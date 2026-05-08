package com.github.mangila.web1.person.web.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record CreatePersonRequest(
    @NotBlank @Size(min = 2, max = 32) String name,
    @PastOrPresent LocalDate birthDate,
    @NotBlank @Email String email,
    @NotNull @Valid List<PhoneDto> phones,
    @NotNull Map<String, String> properties) {}
