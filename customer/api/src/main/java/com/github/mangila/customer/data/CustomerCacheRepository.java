package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.customer.rest.dto.CustomerDto;
import com.github.mangila.integration.pgcache.TtlCacheEntity;
import com.github.mangila.integration.pgcache.TtlCacheRepository;
import com.github.mangila.shared.JsonService;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class CustomerCacheRepository {


    private final JsonService jsonService;
    private final Cache cache;
    private final TtlCacheRepository ttlCacheRepository;

    public CustomerCacheRepository(JsonService jsonService,
                                   @CacheName("customer") Cache cache,
                                   TtlCacheRepository ttlCacheRepository) {
        this.jsonService = jsonService;
        this.cache = cache;
        this.ttlCacheRepository = ttlCacheRepository;
    }

    @Nullable
    public CustomerDto getIfPresent(String key) {
        final CompletableFuture<CustomerDto> l1 = cache.as(CaffeineCache.class)
                .getIfPresent(key);
        if (l1 != null) {
            Log.info("L1 Hit");
            return l1.join();
        }
        final TtlCacheEntity l2 = ttlCacheRepository.findNotExpired(key);
        if (l2 != null) {
            Log.info("L2 Hit");
            var dto = jsonService.toPojo(l2.getPayload(), CustomerDto.class);
            putl1(key, dto);
            return dto;
        }
        Log.info("Cache Miss");
        return null;
    }

    public void putl1(String key, CustomerDto dto) {
        Log.info("L1 Put");
        cache.as(CaffeineCache.class).put(key, CompletableFuture.completedFuture(dto));
    }

    @Transactional
    public void putl2(String key, CustomerDto dto) {
        Log.info("L2 Put");
        final JsonNode node = jsonService.toJson(dto);
        final var ttl = Instant.now().plus(1, ChronoUnit.MILLENNIA);
        final var entity = new TtlCacheEntity(key, node, ttl);
        ttlCacheRepository.persist(entity);
    }

    public void evict(String key) {
        Log.info("L1 Evict");
        var _ = cache.invalidate(key);
    }
}
