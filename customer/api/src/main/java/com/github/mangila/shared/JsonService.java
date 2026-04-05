package com.github.mangila.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.rest.dto.CustomerDto;
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

    public <T> T toPojo(JsonNode json, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T toPojo(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonNode toJson(CustomerDto dto) {
        return objectMapper.valueToTree(dto);
    }
}
