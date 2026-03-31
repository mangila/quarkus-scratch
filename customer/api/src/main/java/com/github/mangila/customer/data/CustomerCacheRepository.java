package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.web.CustomerDto;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

/**
 * L1 Cache
 */
@ApplicationScoped
public class CustomerCacheRepository {

    public void evict(String key) {
        Log.info("Evict L1");
    }

    public CustomerDto getIfPresent(UUID id) {
        return null;
    }
}
