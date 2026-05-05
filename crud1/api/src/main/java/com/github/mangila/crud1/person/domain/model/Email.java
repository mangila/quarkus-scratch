package com.github.mangila.crud1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

public record Email(String value) {

  private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

  public Email {
    ENSURE_STRING_OPS.notBlank(value, "email cannot be null or blank");
  }

  public static Email of(String value) {
    return new Email(value);
  }
}
