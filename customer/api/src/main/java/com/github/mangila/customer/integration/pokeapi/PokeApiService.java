package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @Bulkhead
    @Fallback(PokeApiRestClientFallbackHandler.class)
    public ObjectNode fetchPokemonById(int id) {
        return pokeApiRestClient.fetchPokemonById(id);
    }
}
