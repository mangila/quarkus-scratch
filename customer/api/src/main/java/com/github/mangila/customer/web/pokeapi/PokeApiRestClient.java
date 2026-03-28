package com.github.mangila.customer.web.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "pokeapi")
@ClientHeaderParam(name = "Accept-Encoding", value = "gzip")
public interface PokeApiRestClient {

    @GET
    @Path("/pokemon/{id}")
    ObjectNode getPokemonById(@PathParam("id") int id);
}
