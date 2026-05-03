package com.github.mangila.crud1.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;
import java.util.Map;

public record PersonDto(
        @UUID String id,
        @PastOrPresent LocalDate birthDate,
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotNull Map<String, Object> properties
) {
}
