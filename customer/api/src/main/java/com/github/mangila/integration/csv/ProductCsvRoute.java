package com.github.mangila.integration.csv;

import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.model.dataformat.BindyType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;
import org.jboss.logging.MDC;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class ProductCsvRoute extends RouteBuilder {

    public static final String ROUTE_ID = "product-csv-route";

    private final String uploadsDirectory;
    private final JobRunrScheduler scheduler;
    private final ExecutorService vTexecutor;

    public ProductCsvRoute(@ConfigProperty(name = "quarkus.http.body.uploads-directory") String uploadsDirectory,
                           JobRunrScheduler scheduler,
                           @VirtualThreads ExecutorService vTexecutor) {
        this.uploadsDirectory = uploadsDirectory;
        this.scheduler = scheduler;
        this.vTexecutor = vTexecutor;
    }

    @Override
    public void configure() throws Exception {
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .unmarshal()
                .bindy(BindyType.Csv, ProductCsv.class)
                .split(body())
                .streaming()
                .to("bean-validator://validate")
                .process(exchange -> {
                    var productCsv = exchange.getIn().getBody(ProductCsv.class);
                    MDC.put("product.id", productCsv.getId());
                    vTexecutor.execute(() -> scheduler.schedule(productCsv, Duration.ofSeconds(10)));
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
