package com.github.mangila.customer;

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
import java.util.Objects;

@Dependent
public class CustomerDataSeeder {

    public void seed(@Observes StartupEvent startupEvent,
                     CustomerService customerService) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(true)
                .setDelimiter(',')
                .get();
        try (
                var in = Thread.currentThread().getContextClassLoader().getResourceAsStream("data/customers.csv");
                var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(in), StandardCharsets.UTF_8));
                CSVParser csvParser = csvFormat.parse(reader)) {

            csvParser.forEach(record -> {
                Log.info("Seeding customer: " + record.get(0));
            });

        }
    }

}
