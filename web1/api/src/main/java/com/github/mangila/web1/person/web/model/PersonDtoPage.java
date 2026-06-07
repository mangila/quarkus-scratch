package com.github.mangila.web1.person.web.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;

public record PersonDtoPage(
    @NotNull List<@Valid PersonDto> content,
    @PositiveOrZero long totalCount,
    @PositiveOrZero int pageCount,
    boolean hasNextPage,
    boolean hasPreviousPage) {}
