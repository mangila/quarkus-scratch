package com.github.mangila.customer.web;

import com.github.mangila.customer.config.AppConfig;
import com.github.mangila.customer.shared.CustomerService;
import com.github.mangila.customer.web.pokeapi.PokeApiRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@Path("api/v1/customer")
public class CustomerRestResource {

    private final AppConfig appConfig;
    private final AppConfig.IntegrationConfig integrationConfig;
    private final CustomerService customerService;
    private final PokeApiRestClient pokeApiRestClient;

    public CustomerRestResource(AppConfig appConfig,
                                AppConfig.IntegrationConfig integrationConfig,
                                CustomerService customerService,
                                @RestClient PokeApiRestClient pokeApiRestClient) {
        this.appConfig = appConfig;
        this.integrationConfig = integrationConfig;
        this.customerService = customerService;
        this.pokeApiRestClient = pokeApiRestClient;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        var json = pokeApiRestClient.getPokemonById(1);
        final var map = Map.of(
                "app.secret", appConfig.secret(),
                "integration.secret", integrationConfig.secret(),
                "pokemon", json.toString()
        );
        return map;
    }

}
