package com.github.mangila.web1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;
import java.util.UUID;

public record Id(UUID value) {

  private static final UUID NIL_UUID = new UUID(0L, 0L);

  public Id {
    Ensure.notNull(value, "id cannot be null");
  }

  public static Id newInstance(UUID value) {
    return new Id(value);
  }

  public static Id nil() {
    return new Id(NIL_UUID);
  }

  public boolean isNil() {
    return NIL_UUID.equals(value);
  }
}
