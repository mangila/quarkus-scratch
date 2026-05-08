package com.github.mangila.web1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class IdTest {

  @Test
  void testIsNil() {
    Id id = Id.nil();
    assertThat(id.isNil()).isTrue();
    id = new Id(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    assertThat(id.isNil()).isTrue();
  }
}
