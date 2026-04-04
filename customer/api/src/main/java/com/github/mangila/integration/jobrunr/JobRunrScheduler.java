package com.github.mangila.integration.jobrunr;

import com.github.mangila.integration.csv.CustomerCsvRoute;
import com.github.mangila.integration.csv.ProductCsvRoute;
import com.github.mangila.integration.jobrunr.job.CsvFileUploadJobRequest;
import com.github.mangila.integration.jobrunr.job.CustomerCsvJobRequest;
import com.github.mangila.integration.jobrunr.job.ProductCsvJobRequest;
import com.github.mangila.shared.model.CsvFileUpload;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logmanager.MDC;
import org.jobrunr.scheduling.JobBuilder;
import org.jobrunr.scheduling.JobRequestScheduler;

import java.time.Duration;
import java.util.UUID;

@ApplicationScoped
public class JobRunrScheduler {

    private final JobRequestScheduler scheduler;

    public JobRunrScheduler(JobRequestScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void schedule(CustomerCsvRoute.CustomerCsv csv, Duration delay) {
        Log.info("customer csv");
        final var request = new CustomerCsvJobRequest(csv);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Customer: %s".formatted(csv.getId()))
                .withJobRequest(request)
                .withLabels("customer", "csv")
                .withAmountOfRetries(10);
        scheduler.create(job);
    }

    public void schedule(ProductCsvRoute.ProductCsv csv, Duration delay) {
        Log.info("product csv");
        final var request = new ProductCsvJobRequest(csv);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Product: %s".formatted(csv.getId()))
                .withJobRequest(request)
                .withLabels("product", "csv")
                .withAmountOfRetries(10);
        scheduler.create(job);
    }

    public UUID schedule(CsvFileUpload csv, Duration duration) {
        final var file = csv.file();
        final var originalFileName = file.fileName();
        final var path = file.uploadedFile().toString();
        final var domain = csv.domain().value();
        MDC.put("domain", domain);
        Log.info("file upload");
        final var request = new CsvFileUploadJobRequest(originalFileName, path, domain);
        final var job = JobBuilder.aJob()
                .scheduleIn(duration)
                .withName("Upload: %s".formatted(originalFileName))
                .withJobRequest(request)
                .withLabels("upload", "csv", domain)
                .withAmountOfRetries(0);
        return scheduler.create(job).asUUID();
    }
}
