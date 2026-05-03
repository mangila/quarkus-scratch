package com.github.mangila.crud1.web;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Map;

public record CreatePersonRequest(
        @NotBlank @Size(min = 2, max = 32) String name,
        @PastOrPresent LocalDate birthDate,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotNull Map<String, Object> properties
) {
}
