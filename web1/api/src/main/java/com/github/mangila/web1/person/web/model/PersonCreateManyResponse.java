package com.github.mangila.web1.person.web.model;

import io.github.mangila.ensure4j.Ensure;
import java.util.List;
import java.util.UUID;

public record PersonCreateManyResponse(List<UUID> ids, int count) {

  public PersonCreateManyResponse {
    Ensure.notNull(ids, "Ids cannot be null");
    Ensure.positiveWithZero(count, "Count must be positive or zero");
  }
}
