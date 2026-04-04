package com.github.mangila.integration.jobrunr.job;

import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
import jakarta.enterprise.context.ApplicationScoped;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@ApplicationScoped
public class CustomerCsvJobHandler implements JobRequestHandler<CustomerCsvJobRequest> {

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    public CustomerCsvJobHandler(CustomerMapper customerMapper, CustomerService customerService) {
        this.customerMapper = customerMapper;
        this.customerService = customerService;
    }

    @Override
    public void run(CustomerCsvJobRequest jobRequest) throws Exception {
        var csvRecord = jobRequest.csv();
        var customer = customerMapper.toDomain(csvRecord);
        customerService.save(customer);
    }
}
