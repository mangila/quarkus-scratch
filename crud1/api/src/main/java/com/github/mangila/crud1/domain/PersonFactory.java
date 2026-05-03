package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.shared.UuidFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class PersonFactory {

    private final UuidFactory uuidFactory;

    public PersonFactory(UuidFactory uuidFactory) {
        this.uuidFactory = uuidFactory;
    }

    public Person create(CreatePersonCommand command) {
        final UUID id = uuidFactory.create();
        return new Person(
                id,
                command.name(),
                command.birthDate(),
                command.email(),
                command.phone(),
                command.properties()
        );
    }

}
