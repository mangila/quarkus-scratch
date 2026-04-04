package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.integration.csv.CsvRoute;
import com.github.mangila.shared.exception.CsvException;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.jobrunr.server.runner.ThreadLocalJobContext;

import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class CsvFileUploadJobHandler implements JobRequestHandler<CsvFileUploadJobRequest> {

    private static final String ENDPOINT = "direct:%s".formatted(CsvRoute.ROUTE_ID);

    private final ProducerTemplate producerTemplate;

    public CsvFileUploadJobHandler(ProducerTemplate producerTemplate) {
        this.producerTemplate = new DefaultProducerTemplate(producerTemplate.getCamelContext());
        this.producerTemplate.setDefaultEndpointUri(ENDPOINT);
        this.producerTemplate.start();
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
            producerTemplate.sendBodyAndHeaders(fileName, headers);
        } catch (Exception e) {
            throw new CsvException("Error uploading CSV file: %s".formatted(originalFileName), e);
        }
    }
}
