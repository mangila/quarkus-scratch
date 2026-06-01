package com.github.mangila.web1.person.data;

import java.util.List;

public record PersonEntityPage(
    List<PersonEntity> content,
    long totalCount,
    int pageCount,
    boolean hasNextPage,
    boolean hasPreviousPage) {}
