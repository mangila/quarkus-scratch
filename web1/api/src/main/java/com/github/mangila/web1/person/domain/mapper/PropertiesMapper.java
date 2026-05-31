package com.github.mangila.web1.person.domain.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.web1.person.domain.model.Properties;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class PropertiesMapper implements Mapper<JsonNode, Properties> {

  @Override
  public JsonNode toEntity(Properties properties) {
    return properties.value();
  }

  @Override
  public Properties toDomain(JsonNode jsonNode) {
    return Properties.newInstance(jsonNode);
  }
}
