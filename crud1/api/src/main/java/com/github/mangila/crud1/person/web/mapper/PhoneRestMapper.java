package com.github.mangila.crud1.person.web.mapper;

import com.github.mangila.crud1.person.domain.model.Phone;
import com.github.mangila.crud1.person.web.model.PhoneDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
public final class PhoneRestMapper implements RestMapper<Phone, PhoneDto> {

  @Override
  public Phone toDomain(PhoneDto phoneDto) {
    return new Phone(phoneDto.number(), phoneDto.region(), phoneDto.type());
  }

  @Override
  public PhoneDto toDto(Phone phone) {
    return new PhoneDto(phone.number(), phone.region(), phone.type());
  }
}
