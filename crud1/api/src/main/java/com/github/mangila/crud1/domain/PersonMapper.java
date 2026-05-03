package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.data.PersonEntity;
import com.github.mangila.crud1.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonMapper {

  public Person toDomain(PersonEntity entity) {
    return new Person(
        Id.of(entity.getId()),
        Name.of(entity.getName()),
        BirthDate.of(entity.getBirthDate()),
        Email.of(entity.getEmail()),
        Phone.of(entity.getPhone()),
        Properties.of(entity.getProperties()));
  }

  public PersonEntity toEntity(Person domain) {
    return new PersonEntity(
        domain.id().value(),
        domain.name().value(),
        domain.birthDate().value(),
        domain.email().value(),
        domain.phone().value(),
        domain.properties().value());
  }
}
