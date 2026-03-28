package com.github.mangila.customer.web;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.mangila.ensure4j.Ensure;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerDto(
        @NotNull @org.hibernate.validator.constraints.UUID UUID id,
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank @Email String email,
        @NotBlank String phone,
        JsonNode favoritePokemon
) {

    public CustomerDto {
        if (favoritePokemon != null) {
            Ensure.isTrue(favoritePokemon.isObject(), "favoritePokemon must be an json object");
        }
    }

}
