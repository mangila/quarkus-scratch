package com.github.mangila.shared;

import com.github.mangila.customer.domain.Customer;
import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.shared.exception.CsvException;
import com.github.mangila.shared.model.CsvFileUpload;
import com.github.mangila.shared.model.DomainKey;
import io.github.mangila.ensure4j.Ensure;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class CsvService {

    private final JobRunrScheduler jobRunrScheduler;
    private final Map<DomainKey, String> domainKeyToCsvHeaders;
    private final List<String> supportedMediaTypes;

    public CsvService(JobRunrScheduler jobRunrScheduler) {
        this.jobRunrScheduler = jobRunrScheduler;
        this.domainKeyToCsvHeaders = Map.of(
                DomainKey.customer(), Customer.CSV_HEADERS,
                DomainKey.product(), "id,name,image_url,price",
                DomainKey.order(), "order.csv"
        );
        this.supportedMediaTypes = List.of(MediaType.TEXT_PLAIN);
    }

    public UUID scheduleUpload(CsvFileUpload csv) {
        validateContentType(csv);
        validateHeader(csv);
        return jobRunrScheduler.schedule(csv, Duration.ofSeconds(1));
    }

    private void validateContentType(CsvFileUpload csv) {
        final boolean supported = supportedMediaTypes
                .stream()
                .anyMatch(type -> type.equals(csv.file().contentType()));
        Ensure.isTrue(supported, () -> new CsvException("Not supported media type. Expected: %s, Actual: %s".formatted(supportedMediaTypes, csv.file().contentType())));
    }

    private void validateHeader(CsvFileUpload csv) {
        final var domain = csv.domain();
        final var file = csv.file();
        try (var lines = Files.lines(file.uploadedFile())) {
            String header = lines.findFirst()
                    .orElseThrow(() -> new CsvException("CSV header not found"));
            var headerToCheck = domainKeyToCsvHeaders.get(domain);
            if (!headerToCheck.equals(header)) {
                Log.errorf("CSV header is not valid. Expected: %s, Actual: %s", headerToCheck, header);
                throw new CsvException(String.format("CSV header is not valid. Expected: %s, Actual: %s", headerToCheck, header));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
