package com.github.mangila.crud1.person.web.model;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Map;
import org.hibernate.validator.constraints.UUID;

public record PersonDto(
    @UUID String id,
    @NotBlank @Size(min = 2, max = 32) String name,
    @PastOrPresent LocalDate birthDate,
    @NotBlank @Email String email,
    @NotBlank String phone,
    @NotNull Map<String, Object> properties) {}
