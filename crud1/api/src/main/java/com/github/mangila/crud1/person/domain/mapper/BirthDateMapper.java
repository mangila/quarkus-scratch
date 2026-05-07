package com.github.mangila.crud1.person.domain.mapper;

import com.github.mangila.crud1.person.domain.model.BirthDate;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
public final class BirthDateMapper implements Mapper<LocalDate, BirthDate> {

  @Override
  public LocalDate toEntity(BirthDate birthDate) {
    return birthDate.value();
  }

  @Override
  public BirthDate toDomain(LocalDate localDate) {
    return BirthDate.of(localDate);
  }
}
