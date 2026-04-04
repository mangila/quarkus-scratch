package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.integration.csv.ProductCsvRoute;
import org.jobrunr.jobs.lambdas.JobRequest;

public record ProductCsvJobRequest(ProductCsvRoute.ProductCsv csv) implements JobRequest {

    @Override
    public Class<ProductCsvJobHandler> getJobRequestHandler() {
        return ProductCsvJobHandler.class;
    }
}
