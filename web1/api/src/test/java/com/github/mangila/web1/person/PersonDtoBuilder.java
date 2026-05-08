package com.github.mangila.web1.person;

import com.github.mangila.web1.person.web.model.CreatePersonRequest;
import com.github.mangila.web1.person.web.model.PersonDto;
import com.github.mangila.web1.person.web.model.PhoneDto;
import java.time.LocalDate;
import java.util.*;

public final class PersonDtoBuilder {

  private String id = new UUID(0L, 0L).toString();
  private String name = "John Doe";
  private LocalDate birthDate = LocalDate.of(1993, 12, 10);
  private String email = "john.doe@example.com";
  private final List<PhoneDto> phones = new ArrayList<>();
  private final Map<String, String> properties = new HashMap<>();

  public PersonDtoBuilder id(String id) {
    this.id = id;
    return this;
  }

  public PersonDtoBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PersonDtoBuilder birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public PersonDtoBuilder email(String email) {
    this.email = email;
    return this;
  }

  public PersonDtoBuilder addPhone(PhoneDto phone) {
    this.phones.add(phone);
    return this;
  }

  public PersonDtoBuilder addProperty(String key, String value) {
    this.properties.put(key, value);
    return this;
  }

  public PersonDto build() {
    return new PersonDto(
        this.id, this.name, this.birthDate, this.email, this.phones, this.properties);
  }

  public CreatePersonRequest toCreatePersonRequest() {
    final var phone = new PhoneDto("0736791310", "SE", "mobile");
    addPhone(phone);
    addProperty("city", "Stockholm");
    return new CreatePersonRequest(
        this.name, this.birthDate, this.email, this.phones, this.properties);
  }

  public static PersonDto defaultBuild(String id) {
    final var phone = new PhoneDto("0736791310", "SE", "mobile");
    return new PersonDtoBuilder().id(id).addPhone(phone).addProperty("city", "Sundsvall").build();
  }
}
