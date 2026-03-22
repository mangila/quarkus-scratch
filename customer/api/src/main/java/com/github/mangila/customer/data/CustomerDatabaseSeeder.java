package com.github.mangila.customer.data;

import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
import com.github.mangila.customer.model.Customer;
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
import java.util.ArrayList;
import java.util.Objects;

@Dependent
public class CustomerDatabaseSeeder {

    public void seed(@Observes StartupEvent startupEvent,
                     CustomerMapper customerMapper,
                     CustomerService customerService) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(Customer.CSV_HEADER)
                .setSkipHeaderRecord(true)
                .get();
        try (
                var in = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/customers.csv");
                var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in), StandardCharsets.UTF_8));
                CSVParser csvParser = csvFormat.parse(reader)) {
            final int batchSize = 50;
            var customerBuffer = new ArrayList<Customer>();
            csvParser.stream()
                    .peek(record -> Log.infof("%s", record))
                    .map(customerMapper::toDomain)
                    .peek(customer -> Log.infof("%s", customer))
                    .forEach(customer -> {
                        customerBuffer.add(customer);
                        if (customerBuffer.size() > batchSize) {
                            customerService.saveAll(customerBuffer);
                            customerBuffer.clear();
                        }
                    });
        }
    }

}
