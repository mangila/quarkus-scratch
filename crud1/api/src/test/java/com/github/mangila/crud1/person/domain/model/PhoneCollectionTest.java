package com.github.mangila.crud1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PhoneCollectionTest {

  @Test
  void testPhoneCollectionCreation() {
    PhoneCollection collection = PhoneCollection.newInstance();
    assertThat(collection.value()).isNotNull().isEmpty();
  }

  @Test
  void testPhoneCollectionAddition() {
    PhoneCollection collection = PhoneCollection.newInstance();
    collection.add(Phone.of("0736791310", "SE", "mobile"));
    assertThat(collection.value()).isNotNull().isNotEmpty();
  }

  @Test
  void testEmptyPhoneCollection() {
    PhoneCollection collection = PhoneCollection.empty();
    assertThat(collection.value()).isNotNull().isEmpty();
    final Phone phone = Phone.of("0736791310", "SE", "mobile");
    assertThatThrownBy(() -> collection.add(phone))
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
