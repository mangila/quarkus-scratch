package com.github.mangila.customer.data;

import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Dependent
public class CustomerDatabaseSeeder {


    /**
     * Seeds the initial application state by processing customer data from a CSV file and
     * scheduling tasks for each customer. The CSV file is read, parsed, and the corresponding
     * entities are saved to the database in batches. Additionally, tasks are scheduled with
     * randomized delays for each customer.
     *
     * @param startupEvent An event that triggers application startup, used to identify the
     *                     startup phase of the application lifecycle.
     * @param customerMapper A mapper that converts CSV records into Customer domain objects.
     * @param customerService A service for handling business logic related to Customer entities,
     *                        including persistence operations.
     * @param jobRunrScheduler A scheduler used to schedule tasks for customers asynchronously.
     * @throws IOException If an error occurs while accessing or reading the CSV file.
     */
    public void seed(@Observes StartupEvent startupEvent,
                     CustomerMapper customerMapper,
                     CustomerService customerService,
                     JobRunrScheduler jobRunrScheduler
    ) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(Customer.CSV_HEADER)
                .setSkipHeaderRecord(true)
                .get();
        try (
                var in = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/customers.csv");
                var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in), StandardCharsets.UTF_8));
                CSVParser csvParser = csvFormat.parse(reader)) {
            final int batchSize = 10;
            var customerBuffer = new ArrayList<Customer>();
            csvParser.stream()
                    .peek(record -> Log.infof("%s", record))
                    .map(customerMapper::toDomain)
                    .peek(customer -> Log.infof("%s", customer))
                    .forEach(customer -> {
                        customerBuffer.add(customer);
                        if (customerBuffer.size() > batchSize) {
                            customerService.saveAll(customerBuffer);
                            customerBuffer
                                    .stream()
                                    .map(Customer::id)
                                    .forEach(uuid -> {
                                        var rn = ThreadLocalRandom.current().nextInt(1, 152);
                                        jobRunrScheduler.schedule(rn, uuid, Duration.ofSeconds(rn));
                                    });
                            customerBuffer.clear();
                        }
                    });
        }
    }

}
