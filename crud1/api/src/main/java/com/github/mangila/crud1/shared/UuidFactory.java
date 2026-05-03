package com.github.mangila.crud1.shared;

import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

/**
 * Centralized factory for UUID generation
 */
@ApplicationScoped
public class UuidFactory {

    @NonNull
    public UUID create() {
        return UUID.randomUUID();
    }

    @NonNull
    public UUID from(String id) {
        Ensure.notNull(id, "UUID cannot be null");
        return UUID.fromString(id);
    }
}
