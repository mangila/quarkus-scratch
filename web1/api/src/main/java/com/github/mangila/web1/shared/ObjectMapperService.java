package com.github.mangila.web1.shared;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ObjectMapperService {

  private final ObjectMapper objectMapper;

  public ObjectMapperService(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public JsonNode valueToTree(Object value) {
    return objectMapper.valueToTree(value);
  }

  public <T> T convertValue(JsonNode node, TypeReference<T> typeReference) {
    return objectMapper.convertValue(node, typeReference);
  }
}
