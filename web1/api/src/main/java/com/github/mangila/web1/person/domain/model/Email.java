package com.github.mangila.web1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;

public record Email(String value) {

  public Email {
    Ensure.notBlank(value, "email cannot be null or blank");
  }

  public static Email of(String value) {
    return new Email(value);
  }

  public boolean isGmail() {
    return value.endsWith("@gmail.com");
  }
}
