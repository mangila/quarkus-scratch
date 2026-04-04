package com.github.mangila.customer.rest;

import com.github.mangila.customer.domain.Customer;
import com.github.mangila.shared.CsvService;
import com.github.mangila.shared.exception.CsvException;
import com.github.mangila.shared.model.CsvFileUpload;
import com.github.mangila.shared.model.DomainKey;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.UUID;

@ApplicationScoped
public class CustomerFileServiceRestAdapter {

    private final CsvService csvService;

    public CustomerFileServiceRestAdapter(CsvService csvService) {
        this.csvService = csvService;
    }

    public UUID upload(FileUpload file) {
        try (var lines = Files.lines(file.uploadedFile())) {
            String header = lines.findFirst()
                    .orElseThrow(() -> new CsvException("CSV header not found"));
            if (!Customer.CSV_HEADERS.equals(header)) {
                Log.errorf("CSV header is not valid. Expected: %s, Actual: %s", Customer.CSV_HEADERS, header);
                throw new CsvException(String.format("CSV header is not valid. Expected: %s, Actual: %s", Customer.CSV_HEADERS, header));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return csvService.upload(new CsvFileUpload(file, DomainKey.customer()));
    }
}
