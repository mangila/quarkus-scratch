package com.github.mangila.customer.shared;

import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.data.CustomerRedisRepository;
import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.web.CustomerDto;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CustomerServiceRestAdapter {

    private final CustomerCacheRepository cacheRepository;
    private final CustomerRedisRepository redisRepository;
    private final CustomerMapper mapper;
    private final CustomerService customerService;
    
    public CustomerServiceRestAdapter(CustomerCacheRepository cacheRepository,
                                      CustomerRedisRepository redisRepository, CustomerMapper mapper,
                                      CustomerService customerService) {
        this.cacheRepository = cacheRepository;
        this.redisRepository = redisRepository;
        this.mapper = mapper;
        this.customerService = customerService;
    }

    public CustomerDto findById(UUID id) {
        final CustomerDto l1 = cacheRepository.getIfPresent(id);
        if (l1 != null) {
            return l1;
        }
        final CustomerDto l2 = redisRepository.getIfPresent(id);
        if (l2 != null) {
            return l2;
        }
        final Customer db = customerService.findById(id);
        return mapper.toDto(db);
    }
}
