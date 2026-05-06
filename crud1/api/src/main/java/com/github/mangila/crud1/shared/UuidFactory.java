package com.github.mangila.crud1.shared;

import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

/** Centralized factory for UUID generation */
@ApplicationScoped
@NullMarked
public class UuidFactory {

  public UUID create() {
    return UUID.randomUUID();
  }

  public UUID from(String id) {
    Ensure.notNull(id, "UUID cannot be null");
    return UUID.fromString(id);
  }
}
