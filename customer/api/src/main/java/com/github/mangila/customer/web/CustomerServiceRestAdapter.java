package com.github.mangila.customer.web;

import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.data.CustomerRedisRepository;
import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.integration.jobrunr.JobRunrScheduler;
import com.github.mangila.customer.shared.CustomerFactory;
import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
import com.github.mangila.customer.web.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.web.dto.CustomerDto;
import com.github.mangila.customer.web.cqrs.UpdateCustomerCommand;
import io.quarkiverse.resteasy.problem.HttpProblem;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

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
    private final CustomerFactory factory;
    private final CustomerService customerService;
    private final JobRunrScheduler jobRunrScheduler;
    private final ExecutorService vTexecutor;

    public CustomerServiceRestAdapter(CustomerCacheRepository cacheRepository,
                                      CustomerRedisRepository redisRepository,
                                      CustomerMapper mapper,
                                      CustomerFactory factory,
                                      CustomerService customerService,
                                      JobRunrScheduler jobRunrScheduler,
                                      @VirtualThreads ExecutorService vTexecutor) {
        this.cacheRepository = cacheRepository;
        this.redisRepository = redisRepository;
        this.mapper = mapper;
        this.factory = factory;
        this.customerService = customerService;
        this.jobRunrScheduler = jobRunrScheduler;
        this.vTexecutor = vTexecutor;
    }

    public CustomerDto findById(UUID id) {
        final CustomerDto l1 = cacheRepository.getIfPresent(id);
        if (l1 != null) {
            return l1;
        }
        final CustomerDto l2 = redisRepository.getIfPresent(id);
        if (l2 != null) {
            cacheRepository.put(id, l2);
            return l2;
        }
        return customerService.findById(id)
                .map(customer -> {
                    final var dto = mapper.toDto(customer);
                    vTexecutor.execute(() -> {
                        redisRepository.put(id, dto);
                        cacheRepository.put(id, dto);
                    });
                    return dto;
                })
                .orElseThrow(() -> HttpProblem.valueOf(NOT_FOUND, "Customer not found"));
    }

    public UUID create(CreateCustomerCommand command) {
        Customer customer = factory.from(
                command.name(),
                command.email(),
                command.address(),
                command.phone()
        );
        customerService.save(customer);
        schedulePokemon(command.favoritePokemonId(), customer.id());
        return customer.id();
    }

    public void update(UpdateCustomerCommand command) {
        final Customer domain = mapper.toDomain(command);
        customerService.update(domain);
        schedulePokemon(command.favoritePokemonId(), domain.id());
    }

    public void delete(UUID id) {
        customerService.delete(id);
    }

    private void schedulePokemon(int pokemonId, UUID customerId) {
        if (pokemonId != 0) {
            vTexecutor.execute(() -> jobRunrScheduler.schedule(pokemonId, customerId, Duration.ofSeconds(10)));
        }
    }
}
