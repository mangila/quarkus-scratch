package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.logging.Log;
import io.quarkus.virtual.threads.VirtualThreads;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class PokeApiService {

    private final static String CACHE_KEY_PREFIX = "pokeapi::%s";

    @Inject
    @VirtualThreads
    ExecutorService vTexecutor;

    @Inject
    @RestClient
    PokeApiRestClient pokeApiRestClient;

    @Inject
    PokeApiCache pokeApiCache;

    @Bulkhead
    @NonNull
    public ObjectNode fetchPokemonById(@Positive int pokemonId) {
        var cacheKey = CACHE_KEY_PREFIX.formatted(pokemonId);
        var cacheValue = pokeApiCache.getIfPresent(cacheKey);
        if (cacheValue != null) {
            Log.info("Cache hit");
            return cacheValue.join();
        }
        Log.info("Cache miss");
        var response = pokeApiRestClient.fetchPokemonById(pokemonId);
        if (response.isEmpty()) {
            Log.info("MissingNo");
            response.put("name", "MissingNo");
        } else {
            vTexecutor.execute(() -> {
                Log.info("Cache Put");
                pokeApiCache.put(cacheKey, response);
            });
        }
        return response;
    }
}
