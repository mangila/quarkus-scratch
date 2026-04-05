package com.github.mangila.integration.jobrunr.job;

import org.jobrunr.jobs.lambdas.JobRequest;

public record ProductCsvJobRequest(com.github.mangila.integration.csv.ProductCsvRecord csv) implements JobRequest {

    @Override
    public Class<ProductCsvJobHandler> getJobRequestHandler() {
        return ProductCsvJobHandler.class;
    }
}
