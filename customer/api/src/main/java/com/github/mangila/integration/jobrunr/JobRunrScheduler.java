package com.github.mangila.integration.jobrunr;

import com.github.mangila.integration.csv.CustomerCsvRoute;
import com.github.mangila.integration.csv.ProductCsvRoute;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logmanager.MDC;
import org.jboss.resteasy.reactive.multipart.FileUpload;
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

    public UUID schedule(FileUpload upload, Duration delay) {
        final var originalFileName = upload.fileName();
        final var path = upload.uploadedFile().toString();
        final var domain = "customer";
        MDC.put("domain", domain);
        Log.info("file upload");
        final var request = new CsvUploadJobRequest(originalFileName, path, domain);
        final var job = JobBuilder.aJob()
                .scheduleIn(delay)
                .withName("Upload: %s".formatted(originalFileName))
                .withJobRequest(request)
                .withLabels("upload", "csv", "customer")
                .withAmountOfRetries(10);
        return scheduler.create(job).asUUID();
    }
}
