package com.github.mangila.web1.person.domain;

import com.github.mangila.web1.person.domain.cqrs.PersonCreateCommand;
import com.github.mangila.web1.person.domain.model.Id;
import com.github.mangila.web1.shared.UuidFactory;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class PersonFactory {

  private final UuidFactory uuidFactory;

  public PersonFactory(UuidFactory uuidFactory) {
    this.uuidFactory = uuidFactory;
  }

  public Person newInstance(PersonCreateCommand command) {
    final UUID id = uuidFactory.create();
    return new Person(
        Id.newInstance(id),
        command.name(),
        command.birthDate(),
        command.email(),
        command.phones(),
        command.properties());
  }
}
