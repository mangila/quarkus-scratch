package com.github.mangila.integration.jobrunr.job;

import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@ApplicationScoped
public class ProductCsvJobHandler implements JobRequestHandler<ProductCsvJobRequest> {

    @Override
    public void run(ProductCsvJobRequest jobRequest) throws Exception {

    }
}
