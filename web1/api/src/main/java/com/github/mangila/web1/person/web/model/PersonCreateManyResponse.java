package com.github.mangila.web1.person.web.model;

import java.util.List;
import java.util.UUID;

public record PersonCreateManyResponse(List<UUID> ids, int count) {}
