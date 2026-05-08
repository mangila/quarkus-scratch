package com.github.mangila.web1.person.domain;

import com.github.mangila.web1.person.domain.model.*;
import io.github.mangila.ensure4j.Ensure;

public record Person(
    Id id,
    Name name,
    BirthDate birthDate,
    Email email,
    PhoneCollection phones,
    Properties properties) {

  public Person {
    Ensure.notNull(id, "id cannot be null");
    Ensure.notNull(name, "name cannot be null");
    Ensure.notNull(birthDate, "birth date cannot be null");
    Ensure.notNull(email, "email cannot be null");
    Ensure.notNull(phones, "phones cannot be null");
    Ensure.notNull(properties, "properties cannot be null");
  }
}
