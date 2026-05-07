package com.github.mangila.crud1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BirthDateTest {

  @Test
  void testGetAge() {
    final var date = LocalDate.of(1993, 12, 1);
    final BirthDate birthDate = BirthDate.of(date);
    assertThat(birthDate.age()).isEqualTo(32);
  }
}
