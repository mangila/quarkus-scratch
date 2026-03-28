package com.github.mangila.customer.web.pokeapi;

import com.github.mangila.customer.config.AppConfig;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Simulated filter for an authentication flow for PokeAPI
 * <p>
 * PokeAPI does not provide an authentication flow, this is a simulated filter for scratch purposes
 * This would be a typical filter for an authentication flow with a client credentials grant.
 * <p>
 * During logging, the Authorization header is masked.
 */
@Provider
public class PokeApiRestClientAuthFilter implements ClientRequestFilter {

    private final AppConfig.IntegrationConfig.PokeApi pokeApiConfig;

    public PokeApiRestClientAuthFilter(AppConfig.IntegrationConfig.PokeApi pokeApiConfig) {
        this.pokeApiConfig = pokeApiConfig;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        requestContext.getHeaders()
                .add("Authorization", "Bearer " + pokeApiConfig.token());
    }
}
