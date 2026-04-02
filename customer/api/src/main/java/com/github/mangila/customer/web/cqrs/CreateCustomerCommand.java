package com.github.mangila.customer.web.cqrs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateCustomerCommand(
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @PositiveOrZero int favoritePokemonId
) {
}
