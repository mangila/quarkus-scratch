package com.github.mangila.crud1.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PersonDto(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phone
) {
}
