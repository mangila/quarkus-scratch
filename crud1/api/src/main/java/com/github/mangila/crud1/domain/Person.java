package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.domain.model.*;
import io.github.mangila.ensure4j.Ensure;

import java.util.UUID;

public record Person(
        UUID id,
        Name name,
        BirthDate birthDate,
        Email email,
        Phone phone,
        Properties properties
) {

    public Person {
        Ensure.notNull(id, "id cannot be null");
        Ensure.notNull(name, "name cannot be null");
        Ensure.notNull(birthDate, "birth date cannot be null");
        Ensure.notNull(email, "email cannot be null");
        Ensure.notNull(phone, "phone cannot be null");
        Ensure.notNull(properties, "properties cannot be null");
    }
}