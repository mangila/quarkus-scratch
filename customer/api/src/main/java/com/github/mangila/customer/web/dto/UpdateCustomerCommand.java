package com.github.mangila.customer.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record UpdateCustomerCommand(
        @NotBlank UUID id,
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @PositiveOrZero int favoritePokemonId
) {
}
