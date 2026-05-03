package com.github.mangila.crud1.web;

import com.github.mangila.crud1.domain.Person;
import com.github.mangila.crud1.domain.PersonService;
import com.github.mangila.crud1.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.shared.ApplicationException;
import com.github.mangila.crud1.shared.PersonHttpProblemException;
import com.github.mangila.crud1.shared.UuidFactory;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class PersonRestService {

    private final UuidFactory uuidFactory;
    private final PersonRestMapper personRestMapper;
    private final PersonService personService;

    public PersonRestService(UuidFactory uuidFactory,
                             PersonRestMapper personRestMapper,
                             PersonService personService) {
        this.uuidFactory = uuidFactory;
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
        final UUID uuid = uuidFactory.from(id);
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
        } catch (ApplicationException e) {
            throw new PersonHttpProblemException("Person with id not found: %s".formatted(dto.id()), Status.NOT_FOUND);
        }
    }
}
