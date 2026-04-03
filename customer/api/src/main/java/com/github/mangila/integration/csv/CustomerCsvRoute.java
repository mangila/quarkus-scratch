package com.github.mangila.integration.csv;

import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.model.dataformat.BindyType;
import org.jboss.logging.MDC;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class CustomerCsvRoute extends RouteBuilder {

    public static final String ROUTE_ID = "customer-csv-route";

    private final JobRunrScheduler scheduler;
    private final ExecutorService vTexecutor;

    public CustomerCsvRoute(JobRunrScheduler scheduler, @VirtualThreads ExecutorService vTexecutor) {
        this.scheduler = scheduler;
        this.vTexecutor = vTexecutor;
    }

    @Override
    public void configure() throws Exception {
        final String endpoint = getEndpoint();
        onCompletion()
                .process(_ -> MDC.clear())
                .log("Processed ${body.size} customers");
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .log("Reading customers CSV")
                .pollEnrich(endpoint)
                .log("Reading from ${file:name}")
                .unmarshal()
                .bindy(BindyType.Csv, CustomerCsv.class)
                .split(body())
                .streaming()
                .process(exchange -> {
                    var customer = exchange.getIn().getBody(CustomerCsv.class);
                    MDC.put("customer.id", customer.getId());
                    vTexecutor.execute(() -> scheduler.schedule(customer, Duration.ofSeconds(10)));
                });
    }

    public String getEndpoint() {
        final var dataResource = getCamelContext().getClassResolver()
                .loadResourceAsURL("data");
        return dataResource.toString() + "?fileName=customers.csv";
    }

    @CsvRecord(separator = ",", skipFirstLine = true)
    public static class CustomerCsv {
        // id,name,address,email,phone
        @DataField(pos = 1)
        private String id;

        @DataField(pos = 2)
        private String name;

        @DataField(pos = 3)
        private String address;

        @DataField(pos = 4)
        private String email;

        @DataField(pos = 5)
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
