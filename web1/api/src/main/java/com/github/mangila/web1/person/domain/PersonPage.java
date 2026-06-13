package com.github.mangila.web1.person.domain;

import io.github.mangila.ensure4j.Ensure;
import java.util.List;

public record PersonPage(
    List<Person> content,
    long totalCount,
    int pageCount,
    boolean hasNextPage,
    boolean hasPreviousPage) {

  public PersonPage {
    Ensure.notNull(content, "Content cannot be null");
    Ensure.positiveWithZero(totalCount, "Total count must be positive or zero");
    Ensure.positiveWithZero(pageCount, "Page count must be positive or zero");
  }
}
