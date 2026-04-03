package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.customer.shared.CustomerService;
import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@ApplicationScoped
public class CustomerCsvJobHandler implements JobRequestHandler<CustomerCsvJobRequest> {

    private final CustomerService customerService;

    public CustomerCsvJobHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(CustomerCsvJobRequest jobRequest) throws Exception {
        var csvRecord = jobRequest.csv();
    }
}
