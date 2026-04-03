package com.github.mangila.integration.csv;

import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.model.dataformat.BindyType;
import org.hibernate.validator.constraints.UUID;
import org.jboss.logging.MDC;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class CustomerCsvRoute extends RouteBuilder {

    public static final String ROUTE_ID = "customer-csv";

    private final JobRunrScheduler scheduler;
    private final ExecutorService vTexecutor;

    public CustomerCsvRoute(JobRunrScheduler scheduler,
                            @VirtualThreads ExecutorService vTexecutor) {
        this.scheduler = scheduler;
        this.vTexecutor = vTexecutor;
    }

    @Override
    public void configure() throws Exception {
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .unmarshal()
                .bindy(BindyType.Csv, CustomerCsv.class)
                .split(body())
                .streaming()
                .to("bean-validator://validate")
                .process(exchange -> {
                    var customerCsv = exchange.getIn().getBody(CustomerCsv.class);
                    MDC.put("customer.id", customerCsv.getId());
                    vTexecutor.execute(() -> scheduler.schedule(customerCsv, Duration.ofSeconds(10)));
                });
    }

    @CsvRecord(separator = ",", skipFirstLine = true)
    public static class CustomerCsv {
        @DataField(pos = 1)
        @UUID
        @NotNull
        private String id;

        @DataField(pos = 2)
        @NotBlank
        private String name;

        @DataField(pos = 3)
        @NotBlank
        private String address;

        @DataField(pos = 4)
        @Email
        private String email;

        @DataField(pos = 5)
        @NotBlank
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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
