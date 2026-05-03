package com.github.mangila.crud1.web;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDate;
import java.util.Map;

public record PersonDto(
        @UUID String id,
        @NotBlank @Size(min = 2, max = 32) String name,
        @PastOrPresent LocalDate birthDate,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotNull Map<String, Object> properties
) {
}
