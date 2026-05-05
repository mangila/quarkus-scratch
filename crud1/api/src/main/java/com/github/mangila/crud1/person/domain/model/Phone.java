package com.github.mangila.crud1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

public record Phone(String value) {

  private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

  public Phone {
    ENSURE_STRING_OPS.notBlank(value, "phone cannot be null or blank");
  }

  public static Phone of(String value) {
    return new Phone(value);
  }
}
