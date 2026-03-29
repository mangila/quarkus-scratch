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
     * or just call DEL directly from all replicas,
     * OR Postgres connects to Redis directly and invokes the redis function and send a LISTEN/NOTIFY for the L1 cache
     */
    public void evict(String key) {
        Log.info("Evicting L2 key: " + key);
    }
}
