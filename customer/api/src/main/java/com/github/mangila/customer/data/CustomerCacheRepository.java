package com.github.mangila.customer.data;

import com.github.mangila.customer.web.CustomerDto;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public CustomerDto getIfPresent(@NotNull UUID id) {
        CompletableFuture<CustomerDto> value = cache.as(CaffeineCache.class)
                .getIfPresent(id.toString());
        return value == null ? null : value.join();
    }

    public void evict(@NotBlank String key) {
        Log.info("Evict L1");
        var _ = cache.invalidate(key);
    }
}
