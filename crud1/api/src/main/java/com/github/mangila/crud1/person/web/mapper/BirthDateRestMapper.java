package com.github.mangila.crud1.person.web.mapper;

import com.github.mangila.crud1.person.domain.model.BirthDate;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
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
