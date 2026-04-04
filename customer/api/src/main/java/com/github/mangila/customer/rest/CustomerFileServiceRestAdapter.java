package com.github.mangila.customer.rest;

import com.github.mangila.shared.CsvService;
import com.github.mangila.shared.model.CsvFileUpload;
import com.github.mangila.shared.model.DomainKey;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.UUID;

@ApplicationScoped
public class CustomerFileServiceRestAdapter {

    private final CsvService csvService;

    public CustomerFileServiceRestAdapter(CsvService csvService) {
        this.csvService = csvService;
    }

    public UUID upload(FileUpload file) {
        return csvService.scheduleUpload(new CsvFileUpload(file, DomainKey.customer()));
    }
}
