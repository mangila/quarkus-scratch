package com.github.mangila.web1.person.domain.cqrs;

import io.github.mangila.ensure4j.Ensure;
import java.util.List;
import java.util.stream.Stream;

public record PersonCreateManyCommand(List<PersonCreateCommand> value) {

  public PersonCreateManyCommand {
    Ensure.notNull(value, "value cannot be null");
    Ensure.notEmpty(value, "value cannot be empty");
    Ensure.max(value.size(), 500, "value cannot contain more than 500 elements");
  }

  public Stream<PersonCreateCommand> toStream() {
    return value.stream();
  }
}
