package com.github.mangila.integration.jobrunr;

import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@ApplicationScoped
public class CustomerCsvJobHandler implements JobRequestHandler<CustomerCsvJobRequest> {

    @Override
    public void run(CustomerCsvJobRequest jobRequest) throws Exception {

    }
}
