package com.github.mangila.customer.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.shared.JsonService;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

@Provider
class PokeApiFallbackHandler implements FallbackHandler<ObjectNode> {

    private final JsonService jsonService;

    PokeApiFallbackHandler(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    @Override
    public ObjectNode handle(ExecutionContext context) {
        var node = jsonService.createObjectNode();
        node.put("name", "MissingNo");
        node.put("fallback", true);
        return node;
    }
}
