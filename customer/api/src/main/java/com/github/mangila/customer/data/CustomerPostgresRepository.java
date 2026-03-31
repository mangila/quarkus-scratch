package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.customer.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CustomerPostgresRepository {

    public void persistFavoritePokemon(UUID id, ObjectNode node) {
    }

    public CustomerEntity findById(UUID id) {
        return null;
    }
}
