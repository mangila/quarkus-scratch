package com.github.mangila.crud1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mangila.ensure4j.EnsureException;
import org.junit.jupiter.api.Test;

class PropertiesTest {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

  @Test
  void testPropertiesCreation() {
    final var objectNode = OBJECT_MAPPER.createObjectNode();
    assertThatCode(
            () -> {
              final Properties _ = Properties.of(objectNode);
            })
        .doesNotThrowAnyException();
  }

  @Test
  void testPropertiesCreationWithNull() {
    assertThatThrownBy(
            () -> {
              final Properties _ = Properties.of(null);
            })
        .isInstanceOf(EnsureException.class);
  }

  @Test
  void testPropertiesCreationWithJsonArray() {
    final var jsonArray = OBJECT_MAPPER.createArrayNode();
    assertThatThrownBy(
            () -> {
              final Properties _ = Properties.of(jsonArray);
            })
        .isInstanceOf(EnsureException.class);
  }
}
