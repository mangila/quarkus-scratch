package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.model.Id;
import com.github.mangila.web1.shared.UuidFactory;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public final class IdRestMapper implements RestMapper<Id, String> {

  private final UuidFactory uuidFactory;

  public IdRestMapper(UuidFactory uuidFactory) {
    this.uuidFactory = uuidFactory;
  }

  @Override
  public Id toDomain(String s) {
    final UUID uuid = uuidFactory.from(s);
    return Id.newInstance(uuid);
  }

  @Override
  public String toDto(Id id) {
    return id.value().toString();
  }
}
