package com.github.mangila.web1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;

public record BirthDate(LocalDate value) {

  public BirthDate {
    Ensure.notNull(value, "birth date cannot be null");
    Ensure.pastOrPresent(asInstant(value), Instant.now(), "birth date cannot be in the future");
  }

  public static BirthDate newInstance(LocalDate value) {
    return new BirthDate(value);
  }

  public int age() {
    return Period.between(value, LocalDate.now()).getYears();
  }

  public Instant asInstant(LocalDate date) {
    return date.atStartOfDay().atZone(ZoneOffset.UTC).toInstant();
  }
}
