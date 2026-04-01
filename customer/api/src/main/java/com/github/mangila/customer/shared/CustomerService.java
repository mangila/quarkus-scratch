package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.customer.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for the Customer domain.
 * <p>
 * Here is where we would implement business logic. Before entering
 * the repository layer, we would validate input, perform any necessary
 * transformations, and enforce business rules.
 *
 */
@ApplicationScoped
public class CustomerService {

    private final CustomerPostgresRepository postgresRepository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerPostgresRepository postgresRepository,
                           CustomerMapper mapper) {
        this.postgresRepository = postgresRepository;
        this.mapper = mapper;
    }

    public Optional<Customer> findById(UUID id) {
        return postgresRepository.findById(id)
                .map(mapper::toDomain);
    }

    public void saveAll(List<Customer> customers) {
    }
}
