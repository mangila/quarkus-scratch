package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.integration.csv.CsvDownloadRoute;
import com.github.mangila.shared.exception.ApplicationException;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ProducerTemplate;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.jobrunr.server.runner.ThreadLocalJobContext;

import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class CsvFileDownloadJobHandler implements JobRequestHandler<CsvFileDownloadJobRequest> {

    private static final String ENDPOINT = "direct:%s".formatted(CsvDownloadRoute.ROUTE_ID);

    private final ProducerTemplate producerTemplate;

    public CsvFileDownloadJobHandler(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void run(CsvFileDownloadJobRequest jobRequest) throws Exception {
        final var context = ThreadLocalJobContext.getJobContext();
        final var originalFileName = jobRequest.originalFileName();
        final var domain = jobRequest.domain();
        final var path = Paths.get(jobRequest.path());
        final var fileName = path.getFileName().toString();
        final Map<String, Object> headers = Map.of("domain", domain, "original", originalFileName);
        try {
            producerTemplate.sendBodyAndHeaders(fileName, headers);
        } catch (Exception e) {
            throw new ApplicationException("Error uploading CSV file: %s".formatted(originalFileName), e);
        }
    }
}
