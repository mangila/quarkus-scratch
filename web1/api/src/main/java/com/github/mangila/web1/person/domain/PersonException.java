package com.github.mangila.web1.person.domain;

public class PersonException extends RuntimeException {

  public PersonException(String message) {
    super(message);
  }

  public PersonException(Throwable cause) {
    super(cause);
  }
}
