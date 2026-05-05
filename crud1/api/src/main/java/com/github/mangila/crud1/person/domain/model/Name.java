package com.github.mangila.crud1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

public record Name(String value) {

  private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

  public Name {
    ENSURE_STRING_OPS.notBlank(value, "name cannot be null or blank");
    ENSURE_STRING_OPS.minLength(2, value, "name must be at least 2 characters");
    ENSURE_STRING_OPS.maxLength(32, value, "name cannot be longer than 32 characters");
  }

  public static Name of(String value) {
    return new Name(value);
  }
}
