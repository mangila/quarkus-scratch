package com.github.mangila.integration.jobrunr;

import com.github.mangila.integration.csv.CsvRoute;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class CsvUploadJobHandler implements JobRequestHandler<CsvUploadJobRequest> {

    private final String endpoint = "direct:%s".formatted(CsvRoute.ROUTE_ID);
    private final ProducerTemplate producerTemplate;

    public CsvUploadJobHandler(ProducerTemplate producerTemplate) {
        this.producerTemplate = new DefaultProducerTemplate(producerTemplate.getCamelContext());
        this.producerTemplate.setDefaultEndpointUri(endpoint);
        this.producerTemplate.start();
    }

    @Override
    public void run(CsvUploadJobRequest jobRequest) throws Exception {
        final var originalFileName = jobRequest.originalFileName();
        final var domain = jobRequest.domain();
        final var path = Paths.get(jobRequest.path());
        final var fileName = path.getFileName().toString();
        final Map<String, Object> headers = Map.of("domain", domain, "original", originalFileName);
        producerTemplate.sendBodyAndHeaders(fileName, headers);
        Files.deleteIfExists(path);
    }
}
