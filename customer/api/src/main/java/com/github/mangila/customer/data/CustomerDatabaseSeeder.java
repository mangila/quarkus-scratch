package com.github.mangila.customer.data;

import com.github.mangila.customer.domain.Customer;
import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.jobrunr.utils.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class CustomerDatabaseSeeder {

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;
    private final JobRunrScheduler jobRunrScheduler;

    public CustomerDatabaseSeeder(CustomerMapper customerMapper, CustomerService customerService, JobRunrScheduler jobRunrScheduler) {
        this.customerMapper = customerMapper;
        this.customerService = customerService;
        this.jobRunrScheduler = jobRunrScheduler;
    }

    /**
     * Seeds the initial application state by processing customer data from a CSV file and
     * scheduling tasks for each customer. The CSV file is read, parsed, and the corresponding
     * entities are saved to the database in batches. Additionally, tasks are scheduled with
     * randomized delays for each customer.
     *
     * @param startupEvent An event that triggers an application startup, used to identify the
     *                     startup phase of the application lifecycle.
     * @throws IOException If an error occurs while accessing or reading the CSV file.
     */
    public void seed(@Observes StartupEvent startupEvent) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(Customer.CSV_HEADER)
                .setSkipHeaderRecord(true)
                .get();
        try (
                var in = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/customers.csv");
                var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in, "customers.csv is null"), StandardCharsets.UTF_8));
                CSVParser csvParser = csvFormat.parse(reader)) {
            final int batchSize = 25;
            final var customerBuffer = new ArrayList<Customer>(batchSize);
            csvParser.stream()
                    .map(customerMapper::toDomain)
                    .forEach(customer -> {
                        customerBuffer.add(customer);
                        if (customerBuffer.size() >= batchSize) {
                            handleBatch(customerBuffer);
                            customerBuffer.clear();
                        }
                    });
            if (CollectionUtils.isNotNullOrEmpty(customerBuffer)) {
                handleBatch(customerBuffer);
                customerBuffer.clear();
            }
        }
    }

    /**
     * Double writes to postgres and jobrunr storage provider.
     * Jobrunr PRO supports transaction for double writes.
     * </p>
     * <a href="https://www.jobrunr.io/en/documentation/pro/transactions/">JobRunr transactions</a>
     */
    private void handleBatch(List<Customer> customers) {
        customerService.saveAll(customers);
        customers.stream()
                .map(Customer::id)
                .forEach(this::scheduleCustomerJob);
    }

    public void scheduleCustomerJob(UUID customerId) {
        final int randomPokemonId = getRandomNumber(1, 152);
        final Duration randomDelay = Duration.ofSeconds(getRandomNumber(10, 120));
        //  jobRunrScheduler.schedule(randomPokemonId, customerId, randomDelay);
    }

    public int getRandomNumber(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

}
