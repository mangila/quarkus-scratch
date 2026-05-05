package com.github.mangila.crud1.person.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.mangila.ensure4j.Ensure;

public record Properties(JsonNode value) {

  public Properties {
    Ensure.notNull(value, "properties cannot be null");
    Ensure.isTrue(value.isObject(), "properties must be an object");
  }

  public static Properties of(JsonNode value) {
    return new Properties(value);
  }
}
