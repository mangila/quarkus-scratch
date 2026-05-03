package com.github.mangila.crud1.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.crud1.shared.PersonException;
import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

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
        Ensure.notNull(id, () -> new PersonException("id cannot be null"));
        ENSURE_STRING_OPS.notBlank(name, () -> new PersonException("name cannot be null or blank"));
        ENSURE_STRING_OPS.minLength(2, name, () -> new PersonException("name must be at least 2 characters"));
        ENSURE_STRING_OPS.maxLength(32, name, () -> new PersonException("name cannot be longer than 32 characters"));
        // TODO: ensure4j does not support date time api yet
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new PersonException("birth date cannot be in the future");
        }
        ENSURE_STRING_OPS.notBlank(email, () -> new PersonException("email cannot be blank"));
        ENSURE_STRING_OPS.notBlank(phone, () -> new PersonException("phone phone cannot be blank"));
        Ensure.notNull(properties, () -> new PersonException("properties cannot be null"));
        Ensure.isTrue(properties.isObject(), () -> new PersonException("properties must be an object"));
    }
}