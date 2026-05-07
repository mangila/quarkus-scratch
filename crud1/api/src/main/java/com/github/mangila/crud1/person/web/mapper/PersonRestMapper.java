package com.github.mangila.crud1.person.web.mapper;

import com.github.mangila.crud1.person.domain.Person;
import com.github.mangila.crud1.person.web.model.PersonDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.NullMarked;

@ApplicationScoped
@NullMarked
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
    return new PersonDto(
        idRestMapper.toDto(person.id()),
        nameRestMapper.toDto(person.name()),
        birthDateRestMapper.toDto(person.birthDate()),
        emailRestMapper.toDto(person.email()),
        phoneRestMapper.toDto(person.phones()),
        propertiesRestMapper.toDto(person.properties()));
  }

  @Override
  public Person toDomain(PersonDto dto) {
    return new Person(
        idRestMapper.toDomain(dto.id()),
        nameRestMapper.toDomain(dto.name()),
        birthDateRestMapper.toDomain(dto.birthDate()),
        emailRestMapper.toDomain(dto.email()),
        phoneRestMapper.toDomain(dto.phones()),
        propertiesRestMapper.toDomain(dto.properties()));
  }
}
