package com.github.mangila.customer.data;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * L1 Cache
 */
@ApplicationScoped
public class CustomerCacheRepository {

    public void evict(String key) {
        Log.info("Evict L1");
    }

}
