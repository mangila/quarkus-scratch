package com.github.mangila.crud1.person.domain.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.crud1.person.domain.model.Phone;
import com.github.mangila.crud1.person.domain.model.PhoneCollection;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
public final class PhoneCollectionMapper implements Mapper<JsonNode, PhoneCollection> {

  private final ObjectMapper objectMapper;

  public PhoneCollectionMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public JsonNode toEntity(PhoneCollection phones) {
    return objectMapper.valueToTree(phones.value());
  }

  @Override
  public PhoneCollection toDomain(JsonNode phones) {
    List<Phone> p = objectMapper.convertValue(phones, new TypeReference<>() {});
    return new PhoneCollection(p);
  }
}
