package com.github.mangila.customer.rest;

import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.rest.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.rest.cqrs.UpdateCustomerCommand;
import com.github.mangila.customer.rest.dto.CustomerDto;
import com.github.mangila.customer.shared.CustomerFactory;
import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * REST Adapter for the CustomerService
 * <p>
 * This is where we would implement the business logic for the REST API.
 * It acts as a bridge between the REST API and the service layer, handling
 * requests and responses, and coordinating with the cache and service layers.
 * </p>
 */
@ApplicationScoped
public class CustomerServiceRestAdapter {

    private final CustomerFactory customerFactory;
    private final CustomerMapper mapper;
    private final CustomerCacheRepository cacheRepository;
    private final CustomerService customerService;

    public CustomerServiceRestAdapter(
            CustomerFactory customerFactory,
            CustomerMapper mapper,
            CustomerCacheRepository cacheRepository,
            CustomerService customerService) {
        this.customerFactory = customerFactory;
        this.mapper = mapper;
        this.cacheRepository = cacheRepository;
        this.customerService = customerService;
    }

    public CustomerDto findById(UUID id) {
        final var idAsString = id.toString();
        final CustomerDto cache = cacheRepository.getIfPresent(idAsString);
        if (cache != null) {
            return cache;
        }
        return customerService.findById(id)
                .map(customer -> {
                    var dto = mapper.toDto(customer);
                    cacheRepository.putl2(idAsString, dto);
                    cacheRepository.putl1(idAsString, dto);
                    return dto;
                })
                .orElseThrow(() -> HttpProblem.valueOf(NOT_FOUND, "Customer not found"));
    }

    public UUID create(CreateCustomerCommand command) {
        final Customer customer = customerFactory.from(command);
        customerService.save(customer);
        return customer.id();
    }

    public void update(UpdateCustomerCommand command) {
        final Customer customer = mapper.toDomain(command);
        try {
            customerService.update(customer);
        } catch (EntityNotFoundException e) {
            throw HttpProblem.valueOf(NOT_FOUND, "Customer not found");
        }
    }

    public void delete(UUID id) {
        final boolean deleted = customerService.delete(id);
        if (!deleted) {
            throw HttpProblem.valueOf(NOT_FOUND, "Customer not found");
        }
    }
}
