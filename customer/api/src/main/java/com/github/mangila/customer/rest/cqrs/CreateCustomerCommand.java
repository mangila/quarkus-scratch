package com.github.mangila.customer.rest.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerCommand(
        @NotBlank String name,
        @NotNull JsonNode address,
        @NotBlank @Email String email,
        @NotBlank String phone
) {
}
