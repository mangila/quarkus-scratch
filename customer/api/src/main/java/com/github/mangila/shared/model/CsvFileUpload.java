package com.github.mangila.shared.model;

import org.jboss.resteasy.reactive.multipart.FileUpload;

public record CsvFileUpload(FileUpload file, DomainKey domain) {
}