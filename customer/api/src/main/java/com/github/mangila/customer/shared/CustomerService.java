package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.data.CustomerEntity;
import com.github.mangila.customer.data.CustomerPostgresRepository;
import com.github.mangila.customer.data.CustomerRedisRepository;
import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.web.CustomerDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CustomerService {

    private final CustomerCacheRepository cacheRepository;
    private final CustomerRedisRepository redisRepository;
    private final CustomerPostgresRepository postgresRepository;
    private final CustomerMapper mapper;

    public CustomerService(CustomerCacheRepository cacheRepository,
                           CustomerRedisRepository redisRepository,
                           CustomerPostgresRepository postgresRepository,
                           CustomerMapper mapper) {
        this.cacheRepository = cacheRepository;
        this.redisRepository = redisRepository;
        this.postgresRepository = postgresRepository;
        this.mapper = mapper;
    }

    public Customer findById(UUID id) {
        final CustomerDto l1 = cacheRepository.getIfPresent(id);
        if (l1 != null) {
            return mapper.toDomain(l1);
        }
        final CustomerDto l2 = redisRepository.getIfPresent(id);
        if (l2 != null) {
            return mapper.toDomain(l2);
        }
        final CustomerEntity entity = postgresRepository.findById(id);
        return mapper.toDomain(entity);
    }

    public void saveAll(List<Customer> customers) {
    }
}
