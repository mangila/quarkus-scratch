package com.github.mangila.web1.person;

import com.github.mangila.web1.person.web.model.PersonCreateRequest;
import com.github.mangila.web1.person.web.model.PhoneDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PersonCreateRequestBuilder {

  private String name = "John Doe";
  private LocalDate birthDate = LocalDate.of(1993, 12, 10);
  private String email = "john.doe@example.com";
  private final List<PhoneDto> phones =
      new ArrayList<>(List.of(new PhoneDto("0736791310", "SE", "mobile")));
  private final Map<String, String> properties = new HashMap<>(Map.of("city", "Stockholm"));

  public PersonCreateRequestBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PersonCreateRequestBuilder birthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public PersonCreateRequestBuilder email(String email) {
    this.email = email;
    return this;
  }

  public PersonCreateRequestBuilder addPhone(PhoneDto phone) {
    this.phones.add(phone);
    return this;
  }

  public PersonCreateRequestBuilder addProperty(String key, String value) {
    this.properties.put(key, value);
    return this;
  }

  public PersonCreateRequest build() {
    return new PersonCreateRequest(
        this.name, this.birthDate, this.email, this.phones, this.properties);
  }
}
