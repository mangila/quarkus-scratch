package com.github.mangila.web1.person.domain.mapper;

import com.github.mangila.web1.person.domain.model.Id;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public final class IdMapper implements Mapper<UUID, Id> {

  @Override
  public UUID toEntity(Id id) {
    return id.value();
  }

  @Override
  public Id toDomain(UUID uuid) {
    return Id.newInstance(uuid);
  }
}
