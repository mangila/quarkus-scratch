package com.github.mangila.customer.data;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * L2 Cache
 */
@ApplicationScoped
public class CustomerRedisRepository {

    /**
     * Call a Redis function to let replicas race for a Lock and then run a DEL
     * OR just call DEL directly from all replicas,
     * OR Postgres connects to Redis directly and performs a DEL on redis and sends LISTEN/NOTIFY to the L1 cache(s)
     */
    public void evict(String key) {
        Log.info("Evict L2");
    }
}
