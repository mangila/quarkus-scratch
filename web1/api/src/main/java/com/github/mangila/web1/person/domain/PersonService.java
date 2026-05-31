package com.github.mangila.web1.person.domain;

import com.github.mangila.web1.person.data.PersonDataService;
import com.github.mangila.web1.person.data.PersonEntity;
import com.github.mangila.web1.person.domain.cqrs.CreatePersonCommand;
import com.github.mangila.web1.person.domain.mapper.PersonMapper;
import com.github.mangila.web1.person.domain.model.Id;
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

  public PersonService(
      PersonMapper personMapper, PersonFactory personFactory, PersonDataService personDataService) {
    this.personMapper = personMapper;
    this.personFactory = personFactory;
    this.personDataService = personDataService;
  }

  public List<Person> findPage(Page page) {
    return personDataService.findPage(page).stream().map(personMapper::toDomain).toList();
  }

  public Optional<Person> findById(Id id) {
    final UUID uuid = id.value();
    return personDataService.findById(uuid).map(personMapper::toDomain);
  }

  public UUID create(CreatePersonCommand command) {
    final Person person = personFactory.create(command);
    final PersonEntity entity = personMapper.toEntity(person);
    return personDataService.persist(entity);
  }

  public void update(Person person) throws PersonException {
    final PersonEntity entity = personMapper.toEntity(person);
    try {
      personDataService.update(entity);
    } catch (EntityNotFoundException e) {
      throw new PersonException(e);
    }
  }

  public void delete(Id id) throws PersonException {
    try {
      final UUID uuid = id.value();
      personDataService.delete(uuid);
    } catch (EntityNotFoundException e) {
      throw new PersonException(e);
    }
  }
}
