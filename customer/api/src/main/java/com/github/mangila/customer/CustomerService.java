package com.github.mangila.customer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
}
