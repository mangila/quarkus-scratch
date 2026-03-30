package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jspecify.annotations.NonNull;

@ApplicationScoped
public class PokeApiService {

    @Inject
    @RestClient
    PokeApiRestClient pokeApiRestClient;

    @Inject
    PokeApiCache pokeApiCache;

    @Bulkhead
    @NonNull
    public ObjectNode fetchPokemonById(@Positive int pokemonId) {
        var cache = pokeApiCache.getIfPresent(pokemonId);
        if (cache != null) {
            Log.info("L1 Cache hit");
            return cache.join();
        }
        // L2 check
        Log.info("Cache miss");
        var response = pokeApiRestClient.fetchPokemonById(pokemonId);
        if (response.has("fallback")) {
            Log.info("Fallback");
        } else {
            pokeApiCache.put(pokemonId, response);
        }
        return response;
    }
}
