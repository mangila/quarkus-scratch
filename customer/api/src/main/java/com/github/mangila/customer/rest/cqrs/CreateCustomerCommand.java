package com.github.mangila.customer.rest.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerCommand(
        @NotBlank String name,
        @NotBlank JsonNode address,
        @NotBlank @Email String email,
        @NotBlank String phone
) {
}
