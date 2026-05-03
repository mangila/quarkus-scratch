package com.github.mangila.crud1.domain.cqrs;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;

public record CreatePersonCommand(
        LocalDate birthDate,
        String name,
        String email,
        String phone,
        JsonNode properties
) {
}
