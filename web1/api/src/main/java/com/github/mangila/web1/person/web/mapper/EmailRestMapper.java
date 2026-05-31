package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.model.Email;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class EmailRestMapper implements RestMapper<Email, String> {

  @Override
  public Email toDomain(String s) {
    return Email.newInstance(s);
  }

  @Override
  public String toDto(Email email) {
    return email.value();
  }
}
