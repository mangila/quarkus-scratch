package com.github.mangila.integration.csv;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.shared.JsonService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.model.dataformat.BindyType;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;
import org.jboss.logging.MDC;
import org.jobrunr.server.runner.ThreadLocalJobContext;

import java.time.Duration;

@ApplicationScoped
public class ProductCsvRoute extends RouteBuilder {

    public static final String ROUTE_ID = "product-csv";

    private final JsonService jsonService;
    private final JobRunrScheduler scheduler;

    public ProductCsvRoute(JsonService jsonService,
                           JobRunrScheduler scheduler) {
        this.jsonService = jsonService;
        this.scheduler = scheduler;
    }

    @Override
    public void configure() throws Exception {
        configureBeanValidationExceptionHandling();
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .unmarshal()
                .bindy(BindyType.Csv, ProductCsv.class)
                .to("bean-validator://validate-product")
                .split(body())
                .streaming()
                .process(exchange -> {
                    var productCsv = exchange.getIn().getBody(ProductCsv.class);
                    MDC.put("product.id", productCsv.getId());
                    scheduler.schedule(productCsv, Duration.ofSeconds(10));
                });
    }

    public void configureBeanValidationExceptionHandling() {
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
                    Log.infof("Validation errors: %s", errorJsonArray.toString());
                });
    }

    @CsvRecord(separator = ",", skipFirstLine = true)
    public static class ProductCsv {
        @DataField(pos = 1)
        @UUID
        @NotNull
        private String id;

        @DataField(pos = 2)
        @NotBlank
        private String name;

        @DataField(pos = 3)
        @URL
        private String image_url;

        @DataField(pos = 4)
        @Digits(integer = 10, fraction = 2)
        @NotNull
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
