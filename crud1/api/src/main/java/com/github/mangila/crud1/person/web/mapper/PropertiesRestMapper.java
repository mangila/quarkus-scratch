package com.github.mangila.crud1.person.web.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.crud1.person.domain.model.Properties;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
public final class PropertiesRestMapper implements RestMapper<Properties, Map<String, String>> {

  private final ObjectMapper objectMapper;

  public PropertiesRestMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public Properties toDomain(Map<String, String> propertiesAsMap) {
    final JsonNode properties = objectMapper.valueToTree(propertiesAsMap);
    return Properties.of(properties);
  }

  @Override
  public Map<String, String> toDto(Properties properties) {
    return objectMapper.convertValue(properties.value(), new TypeReference<>() {});
  }
}
