package com.github.mangila.customer.data;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerPostgresRepository {

    public void persistFavoritePokemon(UUID id, ObjectNode node) {
    }

    public Optional<CustomerEntity> findById(UUID id) {
        return Optional.empty();
    }
}
