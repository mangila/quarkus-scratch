package com.github.mangila.customer.domain;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

import java.util.Optional;
import java.util.UUID;

public record Customer(
        UUID id,
        String name,
        String address,
        String email,
        String phone,
        Optional<JsonNode> favoritePokemon
) {

    public static final String[] CSV_HEADER = "id,name,address,email,phone".split(",");

    private static final EnsureStringOps ENSURE_STRING = Ensure.strings();

    public Customer {
        Ensure.notNull(id, "Id cannot be null");
        ENSURE_STRING.notBlank(name, "Name cannot be blank");
        ENSURE_STRING.notBlank(address, "Address cannot be blank");
        ENSURE_STRING.notBlank(email, "Email cannot be blank");
        ENSURE_STRING.notBlank(phone, "Phone cannot be blank");
        Ensure.notNull(favoritePokemon, "Favorite Pokemon cannot be null");
    }

}
