package com.github.mangila.web1.shared;

import static io.quarkiverse.resteasy.problem.HttpProblem.builder;

import io.github.mangila.ensure4j.EnsureException;
import io.quarkiverse.resteasy.problem.ExceptionMapperBase;
import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.ws.rs.ext.Provider;
import java.net.URI;

@Provider
public class EnsureExceptionMapper extends ExceptionMapperBase<EnsureException> {

  private static final URI DEFAULT_TYPE = URI.create("about:blank");
  private static final String DEFAULT_TITLE = "Validation Error";

  @Override
  protected HttpProblem toProblem(EnsureException exception) {
    return builder()
        .withType(DEFAULT_TYPE)
        .withTitle(DEFAULT_TITLE)
        .withDetail(exception.getMessage())
        .withStatus(422)
        .build();
  }
}
