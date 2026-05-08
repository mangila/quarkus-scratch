package com.github.mangila.web1.person.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PhoneDto(
    @NotBlank @Size(min = 5, max = 20) String number,
    @NotBlank @Size(min = 2, max = 2) String region,
    @NotBlank String type) {}
