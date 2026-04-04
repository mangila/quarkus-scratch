package com.github.mangila.customer.rest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CustomerDto(
        @NotNull UUID id,
        @NotBlank String name,
        @NotBlank JsonNode address,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotNull List<UUID> orders
) {
}
