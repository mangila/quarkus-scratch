package com.github.mangila.web1.person.domain;

import java.util.List;

public record PersonPage(
    List<Person> content,
    long totalCount,
    int pageCount,
    boolean hasNextPage,
    boolean hasPreviousPage) {}
