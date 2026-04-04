package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerEntity;
import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.customer.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

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

    @Transactional
    public Optional<Customer> findById(@NotNull UUID id) {
        return postgresRepository.findByIdOptional(id)
                .map(mapper::toDomain);
    }

    @Transactional
    public void save(Customer customer) {
        final var entity = mapper.toEntity(customer);
        postgresRepository.persist(entity);
    }

    @Transactional
    public void update(Customer customer) throws EntityNotFoundException {
        final CustomerEntity entity = postgresRepository.findByIdOptional(customer.id())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        entity.setName(customer.name());
        entity.setAddress(customer.address());
        entity.setEmail(customer.email());
        entity.setPhone(customer.phone());
    }

    @Transactional
    public boolean delete(UUID id) {
        return postgresRepository.deleteById(id);
    }
}
