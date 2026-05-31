package com.github.mangila.web1.person.domain.mapper;

import com.github.mangila.web1.person.domain.model.Name;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class NameMapper implements Mapper<String, Name> {

  @Override
  public String toEntity(Name name) {
    return name.value();
  }

  @Override
  public Name toDomain(String s) {
    return Name.newInstance(s);
  }
}
