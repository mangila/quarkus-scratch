package com.github.mangila.crud1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.crud1.person.data.PersonEntity;
import java.time.LocalDate;
import java.util.UUID;

public final class PersonEntityBuilder {

  public static final UUID NIL_UUID = new UUID(0L, 0L);

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  private UUID id;
  private String name;
  private LocalDate birthDate;
  private String email;
  private String phone;
  private ObjectNode properties;

  public PersonEntityBuilder id(UUID id) {
    this.id = id;
    return this;
  }

  public PersonEntityBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PersonEntityBuilder birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public PersonEntityBuilder email(String email) {
    this.email = email;
    return this;
  }

  public PersonEntityBuilder phone(String phone) {
    this.phone = phone;
    return this;
  }

  public PersonEntityBuilder properties(ObjectNode properties) {
    this.properties = properties;
    return this;
  }

  public PersonEntity build() {
    PersonEntity entity = new PersonEntity();
    entity.setId(this.id);
    entity.setName(this.name);
    entity.setBirthDate(this.birthDate);
    entity.setEmail(this.email);
    entity.setPhone(this.phone);
    entity.setProperties(this.properties);
    return entity;
  }

  public static PersonEntity defaultBuild() {
    return new PersonEntityBuilder()
        .id(NIL_UUID)
        .name("John")
        .birthDate(LocalDate.of(1994, 10, 12))
        .email("john@email.com")
        .phone("0736791310")
        .properties(MAPPER.createObjectNode().put("city", "Stockholm"))
        .build();
  }
}
