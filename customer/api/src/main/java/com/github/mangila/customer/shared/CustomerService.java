package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.customer.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    private final CustomerPostgresRepository customerPostgresRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerPostgresRepository customerPostgresRepository,
                           CustomerMapper customerMapper) {
        this.customerPostgresRepository = customerPostgresRepository;
        this.customerMapper = customerMapper;
    }

    public void saveAll(List<Customer> customers) {
    }
}
