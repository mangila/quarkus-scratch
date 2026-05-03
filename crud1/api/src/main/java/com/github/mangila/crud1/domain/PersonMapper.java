package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.data.PersonEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonMapper {

    public Person toDomain(PersonEntity entity) {
        return new Person(
                entity.getId(),
                entity.getName(),
                entity.getBirthDate(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getProperties()
        );
    }

    public PersonEntity toEntity(Person domain) {
        return new PersonEntity(
                domain.id(),
                domain.birthDate(),
                domain.name(),
                domain.email(),
                domain.phone(),
                domain.properties()
        );
    }
}
