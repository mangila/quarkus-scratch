package com.github.mangila.web1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.mangila.ensure4j.EnsureException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class BirthDateTest {

  @Test
  void testGetAge() {
    final var date = LocalDate.of(1993, 12, 1);
    final BirthDate birthDate = BirthDate.of(date);
    assertThat(birthDate.age()).isEqualTo(32);
  }

  @Test
  void testBirthDatePastOrPresent() {
    final var instant = Instant.now().plus(24, ChronoUnit.DAYS);
    final var date = instant.atOffset(ZoneOffset.UTC).toLocalDate();
    assertThatThrownBy(() -> BirthDate.of(date))
        .isInstanceOf(EnsureException.class)
        .hasMessage("birth date cannot be in the future");
  }
}
