package com.github.mangila.web1.person.data;

import io.github.mangila.ensure4j.Ensure;
import java.util.List;

public record PersonEntityPage(
    List<PersonEntity> content,
    long totalCount,
    int pageCount,
    boolean hasNextPage,
    boolean hasPreviousPage) {

  public PersonEntityPage {
    Ensure.notNull(content, "Content cannot be null");
  }
}
