package com.github.mangila.web1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.web1.person.data.PersonEntity;
import com.github.mangila.web1.person.domain.model.Id;
import com.github.mangila.web1.person.domain.model.Phone;
import java.time.LocalDate;
import java.util.UUID;

public final class PersonEntityBuilder {

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  private UUID id;
  private String name;
  private LocalDate birthDate;
  private String email;
  private final ArrayNode phones = MAPPER.createArrayNode();
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

  public PersonEntityBuilder addPhone(Phone phone) {
    final ObjectNode node = MAPPER.createObjectNode();
    node.put("number", phone.number());
    node.put("region", phone.region());
    node.put("type", phone.type());
    this.phones.add(node);
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
    entity.setPhones(this.phones);
    entity.setProperties(this.properties);
    return entity;
  }

  public static PersonEntity defaultBuild() {
    return new PersonEntityBuilder()
        .id(Id.nil().value())
        .name("John")
        .birthDate(LocalDate.of(1994, 10, 12))
        .email("john@email.com")
        .addPhone(Phone.newInstance("0736791310", "SE", "mobile"))
        .properties(MAPPER.createObjectNode().put("city", "Stockholm"))
        .build();
  }
}
