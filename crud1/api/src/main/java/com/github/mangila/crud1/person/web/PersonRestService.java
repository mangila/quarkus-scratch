package com.github.mangila.crud1.person.web;

import com.github.mangila.crud1.person.domain.Person;
import com.github.mangila.crud1.person.domain.PersonService;
import com.github.mangila.crud1.person.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.person.web.model.CreatePersonRequest;
import com.github.mangila.crud1.person.web.model.PersonDto;
import com.github.mangila.crud1.shared.ApplicationException;
import com.github.mangila.crud1.shared.UuidFactory;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonRestService {

  private final UuidFactory uuidFactory;
  private final PersonRestMapper personRestMapper;
  private final PersonService personService;

  public PersonRestService(
      UuidFactory uuidFactory, PersonRestMapper personRestMapper, PersonService personService) {
    this.uuidFactory = uuidFactory;
    this.personRestMapper = personRestMapper;
    this.personService = personService;
  }

  public List<PersonDto> findPage(Page page) {
    return personService.findPage(page).stream().map(personRestMapper::toDto).toList();
  }

  public PersonDto findById(String id) {
    final UUID uuid = uuidFactory.from(id);
    return personService
        .findById(uuid)
        .map(personRestMapper::toDto)
        .orElseThrow(() -> PersonHttpProblemException.notFound(uuid));
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
      throw PersonHttpProblemException.notFound(dto.id());
    }
  }

  public void delete(String id) {
    final UUID uuid = uuidFactory.from(id);
    try {
      personService.delete(uuid);
    } catch (ApplicationException e) {
      throw PersonHttpProblemException.notFound(uuid);
    }
  }
}
