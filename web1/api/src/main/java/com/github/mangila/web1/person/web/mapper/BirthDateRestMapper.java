package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.model.BirthDate;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

@ApplicationScoped
public final class BirthDateRestMapper implements RestMapper<BirthDate, LocalDate> {

  @Override
  public BirthDate toDomain(LocalDate localDate) {
    return BirthDate.of(localDate);
  }

  @Override
  public LocalDate toDto(BirthDate birthDate) {
    return birthDate.value();
  }
}
