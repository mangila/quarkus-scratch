package com.github.mangila.crud1.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.crud1.domain.Person;
import com.github.mangila.crud1.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.shared.UuidFactory;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class PersonRestMapper {

    private final UuidFactory uuidFactory;
    private final ObjectMapper objectMapper;

    public PersonRestMapper(UuidFactory uuidFactory,
                            ObjectMapper objectMapper) {
        this.uuidFactory = uuidFactory;
        this.objectMapper = objectMapper;
    }

    public PersonDto toDto(Person person) {
        final Map<String, Object> properties = objectMapper.convertValue(
                person.properties(),
                new TypeReference<>() {
                }
        );
        return new PersonDto(
                person.id().toString(),
                person.birthDate(),
                person.name(),
                person.email(),
                person.phone(),
                properties
        );
    }

    public CreatePersonCommand toDomain(CreatePersonRequest request) {
        final JsonNode properties = objectMapper.valueToTree(request.properties());
        return new CreatePersonCommand(
                request.birthDate(),
                request.name(),
                request.email(),
                request.phone(),
                properties
        );
    }

    public Person toDomain(PersonDto dto) {
        final UUID id = uuidFactory.from(dto.id());
        final JsonNode properties = objectMapper.valueToTree(dto.properties());
        return new Person(
                id,
                dto.name(),
                dto.birthDate(),
                dto.email(),
                dto.phone(),
                properties
        );
    }
}
