package com.github.mangila.web1.person.web.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.web1.person.domain.model.Properties;
import com.github.mangila.web1.shared.ObjectMapperService;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public final class PropertiesRestMapper implements RestMapper<Properties, Map<String, String>> {

  private final ObjectMapperService objectMapperService;

  public PropertiesRestMapper(ObjectMapperService objectMapperService) {
    this.objectMapperService = objectMapperService;
  }

  @Override
  public Properties toDomain(Map<String, String> propertiesAsMap) {
    final JsonNode properties = objectMapperService.valueToTree(propertiesAsMap);
    return Properties.newInstance(properties);
  }

  @Override
  public Map<String, String> toDto(Properties properties) {
    return objectMapperService.convertValue(properties.value(), new TypeReference<>() {});
  }
}
