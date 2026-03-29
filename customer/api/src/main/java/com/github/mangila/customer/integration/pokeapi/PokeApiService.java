package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class PokeApiService {

    @Inject
    @RestClient
    PokeApiRestClient pokeApiRestClient;

    @RunOnVirtualThread
    @Fallback(PokeApiRestClientFallbackHandler.class)
    @Bulkhead
    public ObjectNode fetchPokemonById(int id) {
        return pokeApiRestClient.fetchPokemonById(id);
    }
}
