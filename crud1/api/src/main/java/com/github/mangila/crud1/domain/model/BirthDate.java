package com.github.mangila.crud1.domain.model;

import io.github.mangila.ensure4j.Ensure;

import java.time.LocalDate;

public record BirthDate(LocalDate value) {

    public BirthDate {
        // TODO: ensure4j does not support date time api yet
        Ensure.notNull(value, "birth date cannot be null");
        final boolean isAfter = value.isAfter(LocalDate.now());
        Ensure.isTrue(isAfter, "birth date cannot be in the future");
    }

    public static BirthDate of(LocalDate value) {
        return new BirthDate(value);
    }

}
