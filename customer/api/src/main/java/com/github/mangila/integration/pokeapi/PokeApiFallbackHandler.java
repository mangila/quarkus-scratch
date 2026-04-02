package com.github.mangila.integration.pokeapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.shared.JsonService;
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
        return jsonService.createObjectNode();
    }
}
