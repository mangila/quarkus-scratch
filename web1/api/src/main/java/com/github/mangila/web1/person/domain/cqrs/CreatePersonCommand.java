package com.github.mangila.web1.person.domain.cqrs;

import com.github.mangila.web1.person.domain.model.*;

public record CreatePersonCommand(
    Name name, BirthDate birthDate, Email email, PhoneCollection phones, Properties properties) {}
