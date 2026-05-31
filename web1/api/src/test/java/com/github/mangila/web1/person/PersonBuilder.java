package com.github.mangila.web1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.web1.person.domain.Person;
import com.github.mangila.web1.person.domain.model.*;
import java.time.LocalDate;
import java.util.UUID;

public final class PersonBuilder {

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  private UUID id;
  private String name;
  private LocalDate birthDate;
  private String email;
  private final PhoneCollection phones = PhoneCollection.newInstance();
  private ObjectNode properties;

  public PersonBuilder id(UUID id) {
    this.id = id;
    return this;
  }

  public PersonBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PersonBuilder birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public PersonBuilder email(String email) {
    this.email = email;
    return this;
  }

  public PersonBuilder addPhone(Phone phone) {
    this.phones.add(phone);
    return this;
  }

  public PersonBuilder properties(ObjectNode properties) {
    this.properties = properties;
    return this;
  }

  public Person build() {
    return new Person(
        Id.newInstance(this.id),
        Name.newInstance(this.name),
        BirthDate.newInstance(this.birthDate),
        Email.newInstance(this.email),
        phones,
        Properties.newInstance(this.properties));
  }

  public static Person defaultBuild() {
    final var phone = Phone.newInstance("0736791310", "SE", "mobile");
    return new PersonBuilder()
        .id(Id.nil().value())
        .name("John")
        .birthDate(LocalDate.of(1994, 10, 12))
        .email("john@email.com")
        .addPhone(phone)
        .properties(MAPPER.createObjectNode().put("city", "Stockholm"))
        .build();
  }
}
