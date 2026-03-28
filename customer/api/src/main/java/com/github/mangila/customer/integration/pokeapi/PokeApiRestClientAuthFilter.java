package com.github.mangila.customer.integration.pokeapi;

import com.github.mangila.customer.config.AppConfig;
import io.quarkus.logging.Log;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

import java.io.IOException;

/**
 * Simulated filter for an authentication flow for PokeAPI
 * <p>
 * PokeAPI does not provide an authentication flow, this is a simulated filter for scratch purposes
 * This would be a typical filter for an authentication flow with a client credentials grant.
 * <p>
 * During logging, the X-API-TOKEN header is masked.
 */
@Provider
public class PokeApiRestClientAuthFilter implements ClientRequestFilter {

    private final AppConfig.IntegrationConfig.PokeApi pokeApiConfig;

    public PokeApiRestClientAuthFilter(AppConfig.IntegrationConfig.PokeApi pokeApiConfig) {
        this.pokeApiConfig = pokeApiConfig;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MDC.put("X-API-TOKEN", "***");
        requestContext.getHeaders()
                .add("X-API-TOKEN", pokeApiConfig.token());
    }
}
