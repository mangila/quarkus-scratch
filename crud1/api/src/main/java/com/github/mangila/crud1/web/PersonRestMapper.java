package com.github.mangila.crud1.web;

import com.github.mangila.crud1.domain.Person;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PersonRestMapper {

    public List<PersonDto> toDtos(List<Person> persons) {
        return null;
    }

    public PersonDto toDto(Person person) {
        return null;
    }
}
