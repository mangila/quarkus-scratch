package com.github.mangila.web1.person.domain.mapper;

import com.github.mangila.web1.person.domain.model.BirthDate;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

@ApplicationScoped
public final class BirthDateMapper implements Mapper<LocalDate, BirthDate> {

  @Override
  public LocalDate toEntity(BirthDate birthDate) {
    return birthDate.value();
  }

  @Override
  public BirthDate toDomain(LocalDate localDate) {
    return BirthDate.newInstance(localDate);
  }
}
