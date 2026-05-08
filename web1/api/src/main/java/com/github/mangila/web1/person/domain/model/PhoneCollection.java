package com.github.mangila.web1.person.domain.model;

import io.github.mangila.ensure4j.Ensure;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record PhoneCollection(List<Phone> value) {

  private static final PhoneCollection EMPTY = new PhoneCollection(Collections.emptyList());

  public PhoneCollection {
    Ensure.notNull(value, "collection cannot be null");
  }

  public static PhoneCollection newInstance() {
    return new PhoneCollection(new ArrayList<>());
  }

  public static PhoneCollection empty() {
    return EMPTY;
  }

  public void add(Phone phone) {
    value.add(phone);
  }
}
