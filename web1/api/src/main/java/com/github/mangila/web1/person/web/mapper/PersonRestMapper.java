package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.Person;
import com.github.mangila.web1.person.domain.model.*;
import com.github.mangila.web1.person.web.model.PersonDto;
import com.github.mangila.web1.person.web.model.PhoneDto;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public final class PersonRestMapper implements RestMapper<Person, PersonDto> {

  private final IdRestMapper idRestMapper;
  private final NameRestMapper nameRestMapper;
  private final BirthDateRestMapper birthDateRestMapper;
  private final EmailRestMapper emailRestMapper;
  private final PhoneCollectionRestMapper phoneRestMapper;
  private final PropertiesRestMapper propertiesRestMapper;

  public PersonRestMapper(
      IdRestMapper idRestMapper,
      NameRestMapper nameRestMapper,
      BirthDateRestMapper birthDateRestMapper,
      EmailRestMapper emailRestMapper,
      PhoneCollectionRestMapper phoneRestMapper,
      PropertiesRestMapper propertiesRestMapper) {
    this.idRestMapper = idRestMapper;
    this.nameRestMapper = nameRestMapper;
    this.birthDateRestMapper = birthDateRestMapper;
    this.emailRestMapper = emailRestMapper;
    this.phoneRestMapper = phoneRestMapper;
    this.propertiesRestMapper = propertiesRestMapper;
  }

  @Override
  public PersonDto toDto(Person person) {
    final String id = idRestMapper.toDto(person.id());
    final String name = nameRestMapper.toDto(person.name());
    final LocalDate birthDate = birthDateRestMapper.toDto(person.birthDate());
    final String email = emailRestMapper.toDto(person.email());
    final List<PhoneDto> phones = phoneRestMapper.toDto(person.phones());
    final Map<String, String> properties = propertiesRestMapper.toDto(person.properties());
    return new PersonDto(id, name, birthDate, email, phones, properties);
  }

  @Override
  public Person toDomain(PersonDto dto) {
    final Id id = idRestMapper.toDomain(dto.id());
    final Name name = nameRestMapper.toDomain(dto.name());
    final BirthDate birthDate = birthDateRestMapper.toDomain(dto.birthDate());
    final Email email = emailRestMapper.toDomain(dto.email());
    final PhoneCollection phones = phoneRestMapper.toDomain(dto.phones());
    final Properties properties = propertiesRestMapper.toDomain(dto.properties());
    return new Person(id, name, birthDate, email, phones, properties);
  }
}
