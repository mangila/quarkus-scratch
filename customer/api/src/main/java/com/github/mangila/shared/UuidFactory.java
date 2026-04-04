package com.github.mangila.shared;

import com.github.mangila.shared.exception.NotValidUuidException;
import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UuidFactory {

    public UUID create() {
        return UUID.randomUUID();
    }

    public UUID create(String id) throws NotValidUuidException {
        return createSafely(id);
    }

    private UUID createSafely(String id) {
        Ensure.notNull(id, "UUID cannot be null");
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new NotValidUuidException(e.getMessage(), e);
        }
    }

}
