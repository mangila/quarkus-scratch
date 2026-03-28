package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParams;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "pokeapi")
@RegisterProvider(PokeApiRestClientAuthFilter.class)
@ClientHeaderParams(value = {
        @ClientHeaderParam(name = "User-Agent", value = "mangila-customer-api"),
        @ClientHeaderParam(name = "Accept-Encoding", value = "gzip")
})
public interface PokeApiRestClient {

    @GET
    @Path("pokemon/{id}")
    @Fallback(PokeApiRestClientFallbackHandler.class)
    ObjectNode fetchPokemonById(@PathParam("id") int id);
}
