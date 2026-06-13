package com.github.mangila.web1.person.web.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.constraints.UUID;

public record PersonDto(
    @NotNull @UUID String id,
    @NotBlank @Size(min = 2, max = 32) String name,
    @NotNull @PastOrPresent LocalDate birthDate,
    @NotBlank @Email String email,
    @NotEmpty List<@Valid PhoneDto> phones,
    @NotNull Map<@NotBlank String, @NotBlank String> properties) {}
