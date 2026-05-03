package com.github.mangila.crud1.shared;

import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

/**
 * Centralized factory for UUID generation
 */
@ApplicationScoped
public class UuidFactory {

    public UUID create() {
        return UUID.randomUUID();
    }

    public UUID from(String id) throws ApplicationException {
        return fromStringOrThrow(id);
    }

    private static UUID fromStringOrThrow(String id) {
        Ensure.notNull(id, () -> new ApplicationException("UUID cannot be null"));
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

}
