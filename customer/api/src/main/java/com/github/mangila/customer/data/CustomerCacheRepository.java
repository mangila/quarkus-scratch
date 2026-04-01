package com.github.mangila.customer.data;

import com.github.mangila.customer.web.dto.CustomerDto;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
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
        final CompletableFuture<CustomerDto> value = cache.as(CaffeineCache.class)
                .getIfPresent(id.toString());
        if (value != null) {
            Log.info("L1 Cache hit");
            return value.join();
        } else {
            Log.info("L1 Cache miss");
            return null;
        }
    }

    public void evict(@NotBlank String key) {
        Log.info("Evict L1");
        var _ = cache.invalidate(key);
    }

    public void put(@NotNull UUID id, @Valid CustomerDto dto) {
        Log.info("Put L1");
        cache.as(CaffeineCache.class).put(id.toString(), CompletableFuture.completedFuture(dto));
    }
}
