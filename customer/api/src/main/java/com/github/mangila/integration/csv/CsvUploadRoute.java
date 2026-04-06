package com.github.mangila.integration.csv;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.mangila.config.AppConfig;
import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.integration.pgcache.TtlCacheRepository;
import com.github.mangila.shared.JsonService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.dataformat.BindyType;
import org.jboss.logging.MDC;
import org.jobrunr.server.runner.ThreadLocalJobContext;

import java.time.Duration;

@ApplicationScoped
public class CsvUploadRoute extends RouteBuilder {

    public static final String ROUTE_ID = "csv-upload-route";

    private final TtlCacheRepository ttlCacheRepository;
    private final JsonService jsonService;
    private final String fileEndpoint;
    private final JobRunrScheduler scheduler;

    public CsvUploadRoute(AppConfig.IoConfig ioConfig,
                          TtlCacheRepository ttlCacheRepository,
                          JsonService jsonService,
                          JobRunrScheduler scheduler) {
        this.ttlCacheRepository = ttlCacheRepository;
        this.jsonService = jsonService;
        this.scheduler = scheduler;
        this.fileEndpoint = new StringBuilder(128)
                // Uri
                .append("file:")
                .append(ioConfig.uploadDirectory())
                // Params
                .append("?")
                .append("fileName=${body}")
                .append("&")
                .append("delete=true")
                .toString();
    }

    @Override
    public void configure() throws Exception {
        handleBeanValidationException();
        handleIllegalArgumentException();
        handleException();
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .pollEnrich()
                .simple(fileEndpoint)
                .timeout(5000)
                .process(exchange -> {
                    final var fileName = exchange.getMessage().getBody(String.class);
                    MDC.put("fileName", fileName);
                })
                .choice()
                .when(simple("${header.domain} == 'customer'"))
                .unmarshal()
                .bindy(BindyType.Csv, CustomerCsvRecord.class)
                .to("bean-validator://validate-customer")
                .split(body())
                .streaming()
                .process(exchange -> {
                    final var customerCsv = exchange.getMessage()
                            .getBody(CustomerCsvRecord.class);
                    MDC.put("customer.id", customerCsv.getId());
                    scheduler.schedule(customerCsv, Duration.ofSeconds(10));
                })
                .endChoice()
                .when(simple("${header.domain} == 'product'"))
                .unmarshal()
                .bindy(BindyType.Csv, ProductCsvRecord.class)
                .to("bean-validator://validate-product")
                .split(body())
                .streaming()
                .process(exchange -> {
                    final var productCsv = exchange.getMessage()
                            .getBody(ProductCsvRecord.class);
                    MDC.put("product.id", productCsv.getId());
                    scheduler.schedule(productCsv, Duration.ofSeconds(10));
                })
                .endChoice()
                .otherwise()
                .throwException(new IllegalArgumentException("Unknown domain: %s".formatted(header("domain"))))
                .end()
                .log("Completed processing: ${body.size} records")
                .setBody(constant("OK"));
    }

    public void handleBeanValidationException() {
        onException(BeanValidationException.class)
                .handled(false)
                .logExhausted(false)
                .process(exchange -> {
                    final var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BeanValidationException.class);
                    final ArrayNode errorJsonArray = jsonService.createObjectNode().putArray("errors");
                    exception.getConstraintViolations().forEach(violation -> {
                        final String value = violation.getInvalidValue().toString();
                        final String field = violation.getPropertyPath().toString();
                        final String message = violation.getMessage();
                        errorJsonArray.addObject()
                                .put("field", field)
                                .put("value", value)
                                .put("message", message);
                    });
                    final var isJob = ThreadLocalJobContext.hasJobContext();
                    if (isJob) {
                        final var jobContext = ThreadLocalJobContext.getJobContext();
                        jobContext.saveMetadata("errors", errorJsonArray.toString());
                    }
                    exchange.getMessage().setBody(errorJsonArray);
                })
                .log(LoggingLevel.DEBUG, "${exception.message} - ${body}");
    }

    private void handleIllegalArgumentException() {
        onException(IllegalArgumentException.class)
                .handled(false)
                .logExhausted(false)
                .process(exchange -> {
                    final var exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, IllegalArgumentException.class);
                    final ArrayNode errorJsonArray = jsonService.createObjectNode().putArray("errors");
                    errorJsonArray.addObject()
                            .put("message", exception.getMessage());
                    final var isJob = ThreadLocalJobContext.hasJobContext();
                    if (isJob) {
                        final var jobContext = ThreadLocalJobContext.getJobContext();
                        jobContext.saveMetadata("errors", errorJsonArray.toString());
                    }
                    exchange.getMessage().setBody(errorJsonArray);
                })
                .log(LoggingLevel.DEBUG, "${exception.message} - ${body}");
    }

    private void handleException() {
        onException(Exception.class)
                .handled(false)
                .logExhausted(false)
                .process(exchange -> {
                    final ArrayNode errorJsonArray = jsonService.createObjectNode()
                            .putArray("errors");
                    errorJsonArray.addObject()
                            .put("message", "something went wrong");
                    final var isJob = ThreadLocalJobContext.hasJobContext();
                    if (isJob) {
                        final var jobContext = ThreadLocalJobContext.getJobContext();
                        jobContext.saveMetadata("errors", errorJsonArray.toString());
                    }
                    exchange.getMessage().setBody(errorJsonArray);
                })
                .log(LoggingLevel.ERROR, "${exception.message} - ${body}");
    }
}
