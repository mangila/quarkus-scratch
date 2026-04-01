package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerEntity;
import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.customer.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CustomerService {

    private final CustomerPostgresRepository postgresRepository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerPostgresRepository postgresRepository,
                           CustomerMapper mapper) {
        this.postgresRepository = postgresRepository;
        this.mapper = mapper;
    }

    public Customer findById(UUID id) {
        final CustomerEntity entity = postgresRepository.findById(id);
        return mapper.toDomain(entity);
    }

    public void saveAll(List<Customer> customers) {
    }
}
