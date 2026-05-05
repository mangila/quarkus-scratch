package com.github.mangila.crud1.person.domain.cqrs;

import com.github.mangila.crud1.person.domain.model.*;

public record CreatePersonCommand(
    Name name, BirthDate birthDate, Email email, Phone phone, Properties properties) {}
