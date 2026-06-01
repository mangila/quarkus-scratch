package com.github.mangila.web1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.web1.person.data.PersonEntity;
import com.github.mangila.web1.person.domain.model.Phone;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public final class PersonEntityBuilder {

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  private UUID id = new UUID(0L, 0L);
  private String name = "John Doe";
  private LocalDate birthDate = LocalDate.of(1993, 12, 10);
  private String email = "john.doe@example.com";
  private final ArrayNode phones =
      MAPPER
          .createArrayNode()
          .add(
              MAPPER
                  .createObjectNode()
                  .put("number", "0736791310")
                  .put("region", "SE")
                  .put("type", "mobile"));
  private final ObjectNode properties = MAPPER.createObjectNode().put("city", "Stockholm");

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

  public PersonEntityBuilder properties(Map<String, Object> properties) {
    final ObjectNode node = MAPPER.valueToTree(properties);
    this.properties.setAll(node);
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
}
