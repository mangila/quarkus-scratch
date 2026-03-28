package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.shared.JsonService;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;
import org.jboss.logging.MDC;

@Provider
public class PokeApiRestClientFallbackHandler implements FallbackHandler<ObjectNode> {

    private final JsonService jsonService;

    public PokeApiRestClientFallbackHandler(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    @Override
    public ObjectNode handle(ExecutionContext context) {
        MDC.put("pokeapi-fallback", "true");
        var node = jsonService.createObjectNode();
        node.put("name", "MissingNo");
        return node;
    }
}
