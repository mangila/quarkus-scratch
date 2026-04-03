package com.github.mangila.customer.web;

import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.shared.CustomerService;
import com.github.mangila.customer.web.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.web.cqrs.UpdateCustomerCommand;
import com.github.mangila.customer.web.dto.CustomerDto;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

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

    private final CustomerCacheRepository cacheRepository;
    private final CustomerService customerService;
    private final ExecutorService vTexecutor;

    public CustomerServiceRestAdapter(CustomerCacheRepository cacheRepository,
                                      CustomerService customerService,
                                      @VirtualThreads ExecutorService vTexecutor) {
        this.cacheRepository = cacheRepository;
        this.customerService = customerService;
        this.vTexecutor = vTexecutor;
    }

    public CustomerDto findById(UUID id) {
        final CustomerDto cache = cacheRepository.getIfPresent(id);
        if (cache != null) {
            return cache;
        }
        return null;
              //  .orElseThrow(() -> HttpProblem.valueOf(NOT_FOUND, "Customer not found"));
    }

    public UUID create(CreateCustomerCommand command) {
        customerService.save(null);
        return null;
    }

    public void update(UpdateCustomerCommand command) {
        customerService.update(null);
    }

    public void delete(UUID id) {
        customerService.delete(id);
    }
}
