package com.github.mangila.product.rest;

import com.github.mangila.shared.model.CsvFileUpload;
import com.github.mangila.shared.CsvService;
import com.github.mangila.shared.model.DomainKey;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.UUID;

@ApplicationScoped
public class ProductFileServiceRestAdapter {

    private final CsvService csvService;

    public ProductFileServiceRestAdapter(CsvService csvService) {
        this.csvService = csvService;
    }

    public UUID upload(FileUpload file) {
        return csvService.upload(new CsvFileUpload(file, DomainKey.product()));
    }
}
