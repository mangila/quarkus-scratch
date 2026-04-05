package com.github.mangila.integration.jobrunr.job;

import org.jobrunr.jobs.lambdas.JobRequest;

public record CustomerCsvJobRequest(com.github.mangila.integration.csv.CustomerCsvRecord csv) implements JobRequest {

    @Override
    public Class<CustomerCsvJobHandler> getJobRequestHandler() {
        return CustomerCsvJobHandler.class;
    }
}
