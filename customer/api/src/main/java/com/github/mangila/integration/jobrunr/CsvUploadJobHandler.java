package com.github.mangila.integration.jobrunr;

import com.github.mangila.integration.csv.CustomerCsvRoute;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.ProducerTemplate;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class CsvUploadJobHandler implements JobRequestHandler<CsvUploadJobRequest> {

    private final ProducerTemplate producerTemplate;

    public CsvUploadJobHandler(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @Override
    public void run(CsvUploadJobRequest jobRequest) throws Exception {
        final var originalFileName = jobRequest.originalFileName();
        final var route = jobRequest.route();
        final var path = jobRequest.path();
        final var fileName = Paths.get(jobRequest.path()).getFileName().toString();
        Log.infof("CSV upload job started for route: %s, path: %s", route, fileName);
        producerTemplate.sendBody("direct:%s".formatted(CustomerCsvRoute.ROUTE_ID), fileName);
        Files.deleteIfExists(Paths.get(path));
    }
}
