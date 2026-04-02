package com.github.mangila.customer.data;

import com.github.mangila.customer.web.dto.CustomerDto;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * L1 Cache
 */
@ApplicationScoped
public class CustomerCacheRepository {

    private final Cache cache;

    public CustomerCacheRepository(@CacheName("customer") Cache cache) {
        this.cache = cache;
    }

    @Nullable
    public CustomerDto getIfPresent(UUID id) {
        final CompletableFuture<CustomerDto> value = cache.as(CaffeineCache.class)
                .getIfPresent(id.toString());
        if (value != null) {
            Log.info("L1 Hit");
            return value.join();
        } else {
            Log.info("L1 Miss");
            return null;
        }
    }

    public void put(UUID id, CustomerDto dto) {
        Log.info("L1 Put");
        cache.as(CaffeineCache.class).put(id.toString(), CompletableFuture.completedFuture(dto));
    }

    public void evict(String key) {
        Log.info("L1 Evict");
        var _ = cache.invalidate(key);
    }
}
