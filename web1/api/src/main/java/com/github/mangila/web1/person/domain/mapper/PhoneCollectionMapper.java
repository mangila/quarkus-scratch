package com.github.mangila.web1.person.domain.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.web1.person.domain.model.Phone;
import com.github.mangila.web1.person.domain.model.PhoneCollection;
import com.github.mangila.web1.shared.ObjectMapperService;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class PhoneCollectionMapper implements Mapper<JsonNode, PhoneCollection> {

  private final ObjectMapperService objectMapperService;

  public PhoneCollectionMapper(ObjectMapperService objectMapperService) {
    this.objectMapperService = objectMapperService;
  }

  @Override
  public JsonNode toEntity(PhoneCollection phones) {
    return objectMapperService.valueToTree(phones.value());
  }

  @Override
  public PhoneCollection toDomain(JsonNode phones) {
    final List<Phone> p = objectMapperService.convertValue(phones, new TypeReference<>() {});
    return new PhoneCollection(p);
  }
}
