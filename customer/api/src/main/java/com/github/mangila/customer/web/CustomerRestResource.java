package com.github.mangila.customer.web;

import com.github.mangila.customer.config.AppConfig;
import com.github.mangila.customer.integration.pokeapi.PokeApiService;
import com.github.mangila.customer.shared.CustomerService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;

import java.util.Map;

@Path("api/v1/customers")
public class CustomerRestResource {

    private final AppConfig.IntegrationConfig.PokeApi pokeApiConfig;
    private final CustomerService customerService;
    private final PokeApiService pokeApiService;
    private final ProducerTemplate producerTemplate;

    public CustomerRestResource(AppConfig.IntegrationConfig.PokeApi pokeApiConfig,
                                CustomerService customerService,
                                PokeApiService pokeApiService,
                                ProducerTemplate producerTemplate) {
        this.pokeApiConfig = pokeApiConfig;
        this.customerService = customerService;
        this.pokeApiService = pokeApiService;
        this.producerTemplate = producerTemplate;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public Map<String, Object> hello() {
        final int pokemonId = 151;
        MDC.put("pokemonId", String.valueOf(pokemonId));
        var json = pokeApiService.fetchPokemonById(pokemonId);
        Log.info(pokeApiConfig.token());
        final var map = Map.of(
                "pokeapi_token", pokeApiConfig.token(),
                "pokemon", json.get("name").asText()
        );
        producerTemplate.sendBody("pgevent://postgres/customer_evict?datasource=#pgevent", "hello");
        return map;
    }

}
