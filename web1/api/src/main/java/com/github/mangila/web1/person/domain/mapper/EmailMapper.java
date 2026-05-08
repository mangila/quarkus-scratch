package com.github.mangila.web1.person.domain.mapper;

import com.github.mangila.web1.person.domain.model.Email;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class EmailMapper implements Mapper<String, Email> {

  @Override
  public String toEntity(Email email) {
    return email.value();
  }

  @Override
  public Email toDomain(String s) {
    return Email.of(s);
  }
}
