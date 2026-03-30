package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
class PokeApiCache {

    private final Cache cache;

    PokeApiCache(@CacheName("pokeapi") Cache cache) {
        this.cache = cache;
    }

    public CompletableFuture<ObjectNode> getIfPresent(int key) {
        return cache.as(CaffeineCache.class).getIfPresent(key);
    }

    public void put(int key, ObjectNode value) {
        cache.as(CaffeineCache.class).put(key, CompletableFuture.completedFuture(value));
    }
}
