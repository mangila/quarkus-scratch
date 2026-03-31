package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
class PokeApiCache {

    private final Cache cache;
    private final ValueCommands<String, ObjectNode> valueCommands;

    PokeApiCache(@CacheName("pokeapi") Cache cache,
                 RedisDataSource redisDataSource) {
        this.cache = cache;
        this.valueCommands = redisDataSource.value(String.class, ObjectNode.class);
    }

    @Nullable
    public CompletableFuture<ObjectNode> getIfPresent(String key) {
        CompletableFuture<ObjectNode> l1 = cache.as(CaffeineCache.class).getIfPresent(key);
        if (l1 != null) {
            Log.info("L1 Cache hit");
            return l1.thenApply(ObjectNode::deepCopy);
        }
        ObjectNode l2 = valueCommands.get(key);
        if (l2 != null) {
            Log.info("L2 Cache hit");
            return CompletableFuture.completedFuture(l2.deepCopy());
        }
        return null;
    }

    /**
     * Put in the L1 with a TTL key to not put too much RAM in the replicas box.
     * Since we treat this as static data from pokeapi responses, no need to worry about eviction,
     * and we keep the responses in L2.
     * We could send a pgevent to notify other replicas and fill the L1 cache, but sending a full payload with a pgevent
     * can be too much data for the event pipe.
     */
    public void put(String key, ObjectNode value) {
        valueCommands.set(key, value);
        cache.as(CaffeineCache.class).put(key, CompletableFuture.completedFuture(value));
    }
}
