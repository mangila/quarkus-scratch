package com.github.mangila.crud1.person.web.mapper;

import com.github.mangila.crud1.person.domain.model.Email;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
public final class EmailRestMapper implements RestMapper<Email, String> {

  @Override
  public Email toDomain(String s) {
    return Email.of(s);
  }

  @Override
  public String toDto(Email email) {
    return email.value();
  }
}
