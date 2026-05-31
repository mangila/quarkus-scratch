package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.model.Name;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class NameRestMapper implements RestMapper<Name, String> {

  @Override
  public Name toDomain(String s) {
    return Name.newInstance(s);
  }

  @Override
  public String toDto(Name name) {
    return name.value();
  }
}
