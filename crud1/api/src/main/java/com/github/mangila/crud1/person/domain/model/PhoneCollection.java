package com.github.mangila.crud1.person.domain.model;

import java.util.ArrayList;
import java.util.List;

public record PhoneCollection(List<Phone> value) {

  public static final PhoneCollection EMPTY = new PhoneCollection(List.of());

  public static PhoneCollection newInstance() {
    return new PhoneCollection(new ArrayList<>());
  }

  public void add(Phone phone) {
    value.add(phone);
  }
}
