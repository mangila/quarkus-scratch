package com.github.mangila.crud1.shared;

import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.UUID;

public class PersonHttpProblemException extends HttpProblem {

  public static final String NOT_FOUND_ERROR_MESSAGE_FORMAT = "Person with id not found: %s";

  private static final URI DEFAULT_TYPE = URI.create("about:blank");

  public PersonHttpProblemException(String message, Response.Status status) {
    super(defaultMessage(message, status));
  }

  public static PersonHttpProblemException notFound(UUID id) {
    final String message = NOT_FOUND_ERROR_MESSAGE_FORMAT.formatted(id);
    return new PersonHttpProblemException(message, Response.Status.NOT_FOUND);
  }

  public static PersonHttpProblemException notFound(String id) {
    final String message = NOT_FOUND_ERROR_MESSAGE_FORMAT.formatted(id);
    return new PersonHttpProblemException(message, Response.Status.NOT_FOUND);
  }

  private static Builder defaultMessage(String message, Response.Status status) {
    final String title = status.getReasonPhrase();
    final int statusCode = status.getStatusCode();
    return builder()
        .withType(DEFAULT_TYPE)
        .withTitle(title)
        .withDetail(message)
        .withStatus(statusCode);
  }
}
