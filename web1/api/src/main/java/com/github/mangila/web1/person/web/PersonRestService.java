package com.github.mangila.web1.person.web;

import com.github.mangila.web1.person.domain.Person;
import com.github.mangila.web1.person.domain.PersonException;
import com.github.mangila.web1.person.domain.PersonService;
import com.github.mangila.web1.person.domain.cqrs.CreatePersonCommand;
import com.github.mangila.web1.person.domain.model.Id;
import com.github.mangila.web1.person.web.mapper.CreatePersonRestMapper;
import com.github.mangila.web1.person.web.mapper.IdRestMapper;
import com.github.mangila.web1.person.web.mapper.PersonRestMapper;
import com.github.mangila.web1.person.web.model.PersonCreateRequest;
import com.github.mangila.web1.person.web.model.PersonDto;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PersonRestService {

  private final IdRestMapper idRestMapper;
  private final CreatePersonRestMapper createPersonRestMapper;
  private final PersonRestMapper personRestMapper;
  private final PersonService personService;

  public PersonRestService(
      IdRestMapper idRestMapper,
      CreatePersonRestMapper createPersonRestMapper,
      PersonRestMapper personRestMapper,
      PersonService personService) {
    this.idRestMapper = idRestMapper;
    this.createPersonRestMapper = createPersonRestMapper;
    this.personRestMapper = personRestMapper;
    this.personService = personService;
  }

  public List<PersonDto> findPage(Page page) {
    return personService.findPage(page).stream().map(personRestMapper::toDto).toList();
  }

  @CacheResult(cacheName = "persons")
  public PersonDto findById(@CacheKey String id) {
    final Id domainId = idRestMapper.toDomain(id);
    return personService
        .findById(domainId)
        .map(personRestMapper::toDto)
        .orElseThrow(() -> PersonHttpProblemException.notFound(id));
  }

  public UUID create(PersonCreateRequest request) {
    final CreatePersonCommand command = createPersonRestMapper.toDomain(request);
    return personService.create(command);
  }

  @CacheInvalidate(cacheName = "persons", keyGenerator = PersonDtoCacheKeyGenerator.class)
  public void update(PersonDto dto) {
    final Person person = personRestMapper.toDomain(dto);
    try {
      personService.update(person);
    } catch (PersonException _) {
      throw PersonHttpProblemException.notFound(dto.id());
    }
  }

  @CacheInvalidate(cacheName = "persons")
  public void delete(@CacheKey String id) {
    final Id domainId = idRestMapper.toDomain(id);
    try {
      personService.delete(domainId);
    } catch (PersonException _) {
      throw PersonHttpProblemException.notFound(id);
    }
  }
}
