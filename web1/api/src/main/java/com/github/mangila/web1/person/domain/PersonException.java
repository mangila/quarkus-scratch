package com.github.mangila.web1.person.domain;

import java.io.Serial;

public class PersonException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1L;

  public PersonException(String message) {
    super(message);
  }

  public PersonException(Throwable cause) {
    super(cause);
  }
}
