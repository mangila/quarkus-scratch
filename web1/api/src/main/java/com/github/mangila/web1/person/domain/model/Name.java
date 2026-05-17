package com.github.mangila.web1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;

public record Name(String value) {

  public Name {
    Ensure.notBlank(value, "name cannot be null or blank");
    Ensure.minLength(value, 2, "name must be at least 2 characters");
    Ensure.maxLength(value, 32, "name cannot be longer than 32 characters");
  }

  public static Name of(String value) {
    return new Name(value);
  }
}
