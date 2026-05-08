package com.github.mangila.web1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PhoneTest {

  @Test
  void testPhoneCreation() {
    Phone phone = Phone.of("0736791310", "sE", "mobile");
    assertThat(phone)
        .isNotNull()
        .extracting("number", "region", "type")
        .containsExactly("+46736791310", "SE", "MOBILE");
  }
}
