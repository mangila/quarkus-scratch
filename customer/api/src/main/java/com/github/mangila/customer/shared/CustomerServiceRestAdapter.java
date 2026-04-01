package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.data.CustomerRedisRepository;
import com.github.mangila.customer.web.CustomerDto;
import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

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

    private final CustomerCacheRepository cacheRepository;
    private final CustomerRedisRepository redisRepository;
    private final CustomerMapper mapper;
    private final CustomerService customerService;

    public CustomerServiceRestAdapter(CustomerCacheRepository cacheRepository,
                                      CustomerRedisRepository redisRepository,
                                      CustomerMapper mapper,
                                      CustomerService customerService) {
        this.cacheRepository = cacheRepository;
        this.redisRepository = redisRepository;
        this.mapper = mapper;
        this.customerService = customerService;
    }

    public CustomerDto findById(UUID id) {
        final CustomerDto l1 = cacheRepository.getIfPresent(id);
        if (l1 != null) {
            Log.info("L1 Cache hit");
            return l1;
        }
        final CustomerDto l2 = redisRepository.getIfPresent(id);
        if (l2 != null) {
            Log.info("L2 Cache hit");
            return l2;
        }
        Log.info("Cache miss");
        return customerService.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> HttpProblem.valueOf(NOT_FOUND, "Customer not found"));
    }
}
