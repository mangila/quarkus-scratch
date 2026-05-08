package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.model.Phone;
import com.github.mangila.web1.person.domain.model.PhoneCollection;
import com.github.mangila.web1.person.web.model.PhoneDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class PhoneCollectionRestMapper
    implements RestMapper<PhoneCollection, List<PhoneDto>> {

  private final PhoneRestMapper phoneRestMapper;

  public PhoneCollectionRestMapper(PhoneRestMapper phoneRestMapper) {
    this.phoneRestMapper = phoneRestMapper;
  }

  @Override
  public PhoneCollection toDomain(List<PhoneDto> phoneDtos) {
    final List<Phone> phones = phoneDtos.stream().map(phoneRestMapper::toDomain).toList();
    return new PhoneCollection(phones);
  }

  @Override
  public List<PhoneDto> toDto(PhoneCollection phoneCollection) {
    return phoneCollection.value().stream().map(phoneRestMapper::toDto).toList();
  }
}
