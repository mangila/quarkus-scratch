package com.github.mangila.crud1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.crud1.person.domain.Person;
import com.github.mangila.crud1.person.domain.model.*;
import java.time.LocalDate;
import java.util.UUID;

public class TestPersonBuilder {

  public static final UUID NIL_UUID = new UUID(0L, 0L);

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  private UUID id;
  private String name;
  private LocalDate birthDate;
  private String email;
  private String phone;
  private ObjectNode properties;

  public TestPersonBuilder id(UUID id) {
    this.id = id;
    return this;
  }

  public TestPersonBuilder name(String name) {
    this.name = name;
    return this;
  }

  public TestPersonBuilder birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public TestPersonBuilder email(String email) {
    this.email = email;
    return this;
  }

  public TestPersonBuilder phone(String phone) {
    this.phone = phone;
    return this;
  }

  public TestPersonBuilder properties(ObjectNode properties) {
    this.properties = properties;
    return this;
  }

  public Person build() {
    Id id = new Id(this.id);
    Name name = new Name(this.name);
    BirthDate birthDate = new BirthDate(this.birthDate);
    Email email = new Email(this.email);
    Phone phone = new Phone(this.phone);
    Properties properties = new Properties(this.properties);
    return new Person(id, name, birthDate, email, phone, properties);
  }

  public static Person defaultBuild() {
    return new TestPersonBuilder()
        .id(NIL_UUID)
        .name("John")
        .birthDate(LocalDate.of(1994, 10, 12))
        .email("john@email.com")
        .phone("0736791310")
        .properties(MAPPER.createObjectNode().put("city", "Stockholm"))
        .build();
  }
}
