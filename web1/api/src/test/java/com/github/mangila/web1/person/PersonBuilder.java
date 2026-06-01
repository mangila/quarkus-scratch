package com.github.mangila.web1.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.web1.person.domain.Person;
import com.github.mangila.web1.person.domain.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class PersonBuilder {

  private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  private Id id = Id.nil();
  private Name name = Name.newInstance("John Doe");
  private BirthDate birthDate = BirthDate.newInstance(LocalDate.of(1993, 12, 10));
  private Email email = Email.newInstance("john.doe@example.com");
  private final PhoneCollection phones =
      PhoneCollection.newInstance(List.of(Phone.newInstance("0736791310", "SE", "mobile")));
  private Properties properties =
      Properties.newInstance(MAPPER.createObjectNode().put("city", "Stockholm"));

  public PersonBuilder id(UUID id) {
    this.id = Id.newInstance(id);
    return this;
  }

  public PersonBuilder name(String name) {
    this.name = Name.newInstance(name);
    return this;
  }

  public PersonBuilder birthDate(LocalDate birthDate) {
    this.birthDate = BirthDate.newInstance(birthDate);
    return this;
  }

  public PersonBuilder email(String email) {
    this.email = Email.newInstance(email);
    return this;
  }

  public PersonBuilder addPhone(Phone phone) {
    this.phones.add(phone);
    return this;
  }

  public PersonBuilder properties(ObjectNode properties) {
    this.properties = Properties.newInstance(properties);
    return this;
  }

  public Person build() {
    return new Person(id, name, birthDate, email, phones, properties);
  }
}
