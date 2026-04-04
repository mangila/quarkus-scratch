package com.github.mangila.customer.rest.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateCustomerCommand(
        @NotNull UUID id,
        @NotBlank String name,
        @NotNull JsonNode address,
        @NotBlank @Email String email,
        @NotBlank String phone) {
}
