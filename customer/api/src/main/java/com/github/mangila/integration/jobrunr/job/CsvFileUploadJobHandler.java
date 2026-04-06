package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.integration.csv.CsvRouteProducer;
import com.github.mangila.integration.csv.CsvUploadRoute;
import com.github.mangila.shared.exception.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.jobrunr.server.runner.ThreadLocalJobContext;

import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class CsvFileUploadJobHandler implements JobRequestHandler<CsvFileUploadJobRequest> {

    private static final String ENDPOINT = "direct:%s".formatted(CsvUploadRoute.ROUTE_ID);

    private final CsvRouteProducer csvRouteProducer;

    public CsvFileUploadJobHandler(CsvRouteProducer csvRouteProducer) {
        this.csvRouteProducer = csvRouteProducer;
    }

    @Override
    public void run(CsvFileUploadJobRequest jobRequest) throws Exception {
        final var context = ThreadLocalJobContext.getJobContext();
        final var originalFileName = jobRequest.originalFileName();
        final var domain = jobRequest.domain();
        final var path = Paths.get(jobRequest.path());
        final var fileName = path.getFileName().toString();
        final Map<String, Object> headers = Map.of("domain", domain, "original", originalFileName);
        try {
            var exchange = csvRouteProducer.upload(fileName, headers);
        } catch (Exception e) {
            throw new ApplicationException("Error uploading CSV file: %s".formatted(originalFileName), e);
        }
    }
}
