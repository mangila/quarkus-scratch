package com.github.mangila.crud1.person;

import com.github.mangila.crud1.person.web.model.PersonDto;
import com.github.mangila.crud1.person.web.model.PhoneDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PersonDtoBuilder {

  private String id;
  private String name;
  private LocalDate birthDate;
  private String email;
  private final List<PhoneDto> phones = new ArrayList<>();
  private Map<String, String> properties;

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

  public PersonDtoBuilder properties(Map<String, String> properties) {
    this.properties = properties;
    return this;
  }

  public PersonDto build() {
    return new PersonDto(
        this.id, this.name, this.birthDate, this.email, this.phones, this.properties);
  }

  public static PersonDto defaultBuild(String id) {
    final var phone = new PhoneDto("0736791310", "SE", "mobile");
    return new PersonDtoBuilder()
        .id(id)
        .name("John")
        .birthDate(LocalDate.of(1993, 12, 10))
        .email("john@email.com")
        .addPhone(phone)
        .properties(Map.of("city", "Sundsvall"))
        .build();
  }
}
