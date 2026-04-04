package com.github.mangila.shared;

import com.github.mangila.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.shared.model.CsvFileUpload;
import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.time.Duration;
import java.util.UUID;

@ApplicationScoped
public class CsvService {

    private final JobRunrScheduler jobRunrScheduler;

    public CsvService(JobRunrScheduler jobRunrScheduler) {
        this.jobRunrScheduler = jobRunrScheduler;
    }

    public UUID upload(CsvFileUpload csv) {
        final var file = csv.file();
        Ensure.isTrue(MediaType.TEXT_PLAIN.equals(file.contentType()), "Only text/plain content type is supported");
        return jobRunrScheduler.schedule(csv, Duration.ofSeconds(1));
    }
}
