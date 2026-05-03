package com.github.mangila.crud1.web;

import com.github.mangila.crud1.domain.Person;
import com.github.mangila.crud1.domain.PersonService;
import com.github.mangila.crud1.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.shared.PersonException;
import com.github.mangila.crud1.shared.PersonHttpProblemException;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class PersonRestService {

    private final PersonRestMapper personRestMapper;
    private final PersonService personService;

    public PersonRestService(PersonRestMapper personRestMapper,
                             PersonService personService) {
        this.personRestMapper = personRestMapper;
        this.personService = personService;
    }

    public List<PersonDto> findPage(Page page) {
        return personService.findPage(page)
                .stream()
                .map(personRestMapper::toDto)
                .toList();
    }

    public PersonDto findById(String id) {
        UUID uuid = UUID.fromString(id);
        return personService.findById(uuid)
                .map(personRestMapper::toDto)
                .orElseThrow(() -> new PersonHttpProblemException("Person with id not found: %s".formatted(id), Status.NOT_FOUND));
    }

    public UUID create(CreatePersonRequest request) {
        final CreatePersonCommand command = personRestMapper.toDomain(request);
        final UUID id = personService.create(command);
        return id;
    }

    public void update(PersonDto dto) {
        final Person person = personRestMapper.toDomain(dto);
        try {
            personService.update(person);
        } catch (PersonException e) {
            throw new PersonHttpProblemException(e.getMessage(), Status.NOT_FOUND);
        }
    }
}
