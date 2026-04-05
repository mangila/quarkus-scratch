package com.github.mangila.shared;

import com.github.mangila.shared.exception.ApplicationException;
import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class UuidFactory {

    public UUID create() {
        return UUID.randomUUID();
    }

    public UUID create(String id) throws ApplicationException {
        return createFromStringOrThrow(id);
    }

    private UUID createFromStringOrThrow(String id) {
        Ensure.notNull(id, "UUID cannot be null");
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

}
