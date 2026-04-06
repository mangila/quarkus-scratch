package com.github.mangila.shared;

import com.github.mangila.integration.csv.CsvDownloadRoute;
import com.github.mangila.integration.csv.CustomerCsvRecord;
import com.github.mangila.integration.csv.ProductCsvRecord;
import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.shared.exception.ApplicationException;
import com.github.mangila.shared.model.CsvFileDownload;
import com.github.mangila.shared.model.CsvFileUpload;
import com.github.mangila.shared.model.DomainKey;
import io.github.mangila.ensure4j.Ensure;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;
import org.apache.camel.ProducerTemplate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class CsvService {

    private final ProducerTemplate producerTemplate;
    private final JobRunrScheduler jobRunrScheduler;
    private final Map<DomainKey, String> domainKeyToCsvHeaders;
    private final List<String> supportedMediaTypes;

    public CsvService(ProducerTemplate producerTemplate,
                      JobRunrScheduler jobRunrScheduler) {
        this.producerTemplate = producerTemplate;
        this.jobRunrScheduler = jobRunrScheduler;
        this.domainKeyToCsvHeaders = Map.of(
                DomainKey.customer(), CustomerCsvRecord.CSV_HEADERS,
                DomainKey.product(), ProductCsvRecord.CSV_HEADERS,
                DomainKey.order(), "order.csv"
        );
        this.supportedMediaTypes = List.of(MediaType.TEXT_PLAIN);
    }

    public UUID scheduleDownload(DomainKey domain) {
        return jobRunrScheduler.schedule(new CsvFileDownload(domain), Duration.ofSeconds(1));
    }

    public UUID scheduleUpload(CsvFileUpload csv) {
        try {
            validateContentType(csv);
            validateHeader(csv);
        } catch (Exception e) {
            try {
                Files.deleteIfExists(csv.file().uploadedFile());
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
            throw e;
        }
        return jobRunrScheduler.schedule(csv, Duration.ofSeconds(1));
    }

    private void validateContentType(CsvFileUpload csv) {
        final boolean supported = supportedMediaTypes
                .stream()
                .anyMatch(type -> type.equals(csv.file().contentType()));
        Ensure.isTrue(supported, () -> new ApplicationException("Not supported media type. Expected: %s, Actual: %s".formatted(supportedMediaTypes, csv.file().contentType())));
    }

    private void validateHeader(CsvFileUpload csv) {
        final var domain = csv.domain();
        final var file = csv.file();
        try (var lines = Files.lines(file.uploadedFile())) {
            String header = lines.findFirst()
                    .orElseThrow(() -> new ApplicationException("CSV header not found"));
            var headerToCheck = domainKeyToCsvHeaders.get(domain);
            Ensure.notNull(headerToCheck, () -> new ApplicationException("Domain not supported: %s".formatted(domain)));
            if (!headerToCheck.equals(header)) {
                final var errMsg = "CSV header is not valid. Expected: %s, Actual: %s";
                Log.errorf(errMsg, headerToCheck, header);
                throw new ApplicationException(String.format(errMsg, headerToCheck, header));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Path download(DomainKey domain) {
        var s = producerTemplate.send("direct:%s".formatted(CsvDownloadRoute.ROUTE_ID), exchange -> {
            exchange.getMessage()
                    .setHeader("domain", domain.value());
        });
        return null;
    }
}
