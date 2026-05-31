package com.github.mangila.web1.person.domain.model;

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
              final Properties _ = Properties.newInstance(objectNode);
            })
        .doesNotThrowAnyException();
  }

  @Test
  void testPropertiesCreationWithNull() {
    assertThatThrownBy(
            () -> {
              final Properties _ = Properties.newInstance(null);
            })
        .isInstanceOf(EnsureException.class);
  }

  @Test
  void testPropertiesCreationWithJsonArray() {
    final var jsonArray = OBJECT_MAPPER.createArrayNode();
    assertThatThrownBy(
            () -> {
              final Properties _ = Properties.newInstance(jsonArray);
            })
        .isInstanceOf(EnsureException.class);
  }
}
