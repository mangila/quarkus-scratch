package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.shared.PersonDomainException;
import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;
import tools.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.util.UUID;

public record Person(
        UUID id,
        String name,
        LocalDate birthDate,
        String email,
        String phone,
        JsonNode properties
) {
    private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

    public Person {
        Ensure.notNull(id, () -> new PersonDomainException("id cannot be null"));
        ENSURE_STRING_OPS.notBlank(name, () -> new PersonDomainException("name cannot be null or blank"));
        ENSURE_STRING_OPS.minLength(2, name, () -> new PersonDomainException("name must be at least 2 characters"));
        ENSURE_STRING_OPS.maxLength(32, name, () -> new PersonDomainException("name cannot be longer than 32 characters"));
        // TODO: ensure4j does not support date time api yet
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new PersonDomainException("birth date cannot be in the future");
        }
        ENSURE_STRING_OPS.notBlank(email, () -> new PersonDomainException("email cannot be blank"));
        ENSURE_STRING_OPS.notBlank(phone, () -> new PersonDomainException("phone phone cannot be blank"));
        Ensure.notNull(properties, () -> new PersonDomainException("properties cannot be null"));
        Ensure.isTrue(properties.isObject(), () -> new PersonDomainException("properties must be an object"));
    }
}