package com.github.mangila.customer.web.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateCustomerCommand(
        UUID id,
        @NotBlank String name,
        @NotBlank JsonNode address,
        @NotBlank @Email String email,
        @NotBlank String phone) {
}
