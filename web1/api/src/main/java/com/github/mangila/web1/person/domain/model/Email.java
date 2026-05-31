package com.github.mangila.web1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;

public record Email(String value) {

  public Email {
    Ensure.notBlank(value, "email cannot be null or blank");
    Ensure.matchesEmail(value, "email must be a valid email address");
  }

  public static Email newInstance(String value) {
    return new Email(value);
  }

  public boolean isGmail() {
    return value.endsWith("@gmail.com");
  }
}
