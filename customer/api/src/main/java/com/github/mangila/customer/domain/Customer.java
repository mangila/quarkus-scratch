package com.github.mangila.customer.domain;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

import java.util.List;
import java.util.UUID;

public record Customer(
        UUID id,
        String name,
        JsonNode address,
        String email,
        String phone,
        List<UUID> orders
) {

    public static final String[] CSV_HEADER = "id,name,address,email,phone".split(",");

    private static final EnsureStringOps ENSURE_STRING = Ensure.strings();

    public Customer {
        Ensure.notNull(id, "Id cannot be null");
        ENSURE_STRING.notBlank(name, "Name cannot be blank");
        Ensure.notNull(address, "Address cannot be null");
        ENSURE_STRING.notBlank(email, "Email cannot be blank");
        ENSURE_STRING.notBlank(phone, "Phone cannot be blank");
        Ensure.notNull(orders, "Orders cannot be null");
    }

}
