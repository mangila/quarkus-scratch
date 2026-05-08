package com.github.mangila.web1.shared;

public class ApplicationException extends RuntimeException {

  public ApplicationException(String message) {
    super(message);
  }

  public ApplicationException(Throwable cause) {
    super(cause);
  }
}
