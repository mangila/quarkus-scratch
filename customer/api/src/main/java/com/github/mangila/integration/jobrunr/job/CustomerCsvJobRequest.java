package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.integration.csv.CustomerCsvRoute;
import org.jobrunr.jobs.lambdas.JobRequest;

public record CustomerCsvJobRequest(CustomerCsvRoute.CustomerCsv csv) implements JobRequest {

    @Override
    public Class<CustomerCsvJobHandler> getJobRequestHandler() {
        return CustomerCsvJobHandler.class;
    }
}
