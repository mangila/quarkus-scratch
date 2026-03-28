package com.github.mangila.customer.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * JSON service for centralized ObjectMapper management
 */
@ApplicationScoped
public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }
}
