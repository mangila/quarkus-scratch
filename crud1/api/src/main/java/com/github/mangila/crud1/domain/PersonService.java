package com.github.mangila.crud1.domain;

import com.github.mangila.crud1.data.PersonDataService;
import com.github.mangila.crud1.data.PersonEntity;
import com.github.mangila.crud1.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.shared.ApplicationException;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PersonService {

    private final PersonMapper personMapper;
    private final PersonFactory personFactory;
    private final PersonDataService personDataService;

    public PersonService(PersonMapper personMapper,
                         PersonFactory personFactory,
                         PersonDataService personDataService) {
        this.personMapper = personMapper;
        this.personFactory = personFactory;
        this.personDataService = personDataService;
    }

    public List<Person> findPage(Page page) {
        return personDataService.findPage(page)
                .stream()
                .map(personMapper::toDomain)
                .toList();
    }

    public Optional<Person> findById(UUID id) {
        return personDataService.findById(id)
                .map(personMapper::toDomain);
    }

    public UUID create(CreatePersonCommand command) {
        final Person person = personFactory.create(command);
        final PersonEntity entity = personMapper.toEntity(person);
        final UUID id = personDataService.persist(entity);
        return id;
    }

    public void update(Person person) throws ApplicationException {
        final PersonEntity entity = personMapper.toEntity(person);
        try {
            personDataService.update(entity);
        } catch (EntityNotFoundException e) {
            throw new ApplicationException(e);
        }
    }
}
