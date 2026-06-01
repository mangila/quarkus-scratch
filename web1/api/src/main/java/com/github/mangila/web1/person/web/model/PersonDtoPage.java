package com.github.mangila.web1.person.web.model;

import java.util.List;

public record PersonDtoPage(
    List<PersonDto> content,
    long totalCount,
    int pageCount,
    boolean hasNextPage,
    boolean hasPreviousPage) {}
