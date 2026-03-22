package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerRepository;
import com.github.mangila.customer.model.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public void saveAll(List<Customer> customers) {
    }
}
