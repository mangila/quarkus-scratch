package com.github.mangila.crud1.person.domain.mapper;

import com.github.mangila.crud1.person.domain.model.Name;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
public final class NameMapper implements Mapper<String, Name> {

  @Override
  public String toEntity(Name name) {
    return name.value();
  }

  @Override
  public Name toDomain(String s) {
    return Name.of(s);
  }
}
