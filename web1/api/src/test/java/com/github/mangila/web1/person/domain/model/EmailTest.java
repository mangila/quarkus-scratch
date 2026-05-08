package com.github.mangila.web1.person.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void testIsGmail() {
    final String emailString = "test@gmail.com";
    final Email email = Email.of(emailString);
    assertThat(email.isGmail()).isTrue();
  }

  @Test
  void testIsGmailFalse() {
    final String emailString = "test@yahoo.com";
    final Email email = Email.of(emailString);
    assertThat(email.isGmail()).isFalse();
  }
}
