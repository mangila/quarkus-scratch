package com.github.mangila.web1.person.domain.mapper;

import com.github.mangila.web1.person.data.PersonEntity;
import com.github.mangila.web1.person.domain.Person;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class PersonMapper implements Mapper<PersonEntity, Person> {

  private final IdMapper idMapper;
  private final NameMapper nameMapper;
  private final BirthDateMapper birthDateMapper;
  private final EmailMapper emailMapper;
  private final PhoneCollectionMapper phoneCollectionMapper;
  private final PropertiesMapper propertiesMapper;

  public PersonMapper(
      IdMapper idMapper,
      NameMapper nameMapper,
      BirthDateMapper birthDateMapper,
      EmailMapper emailMapper,
      PhoneCollectionMapper phoneCollectionMapper,
      PropertiesMapper propertiesMapper) {
    this.idMapper = idMapper;
    this.nameMapper = nameMapper;
    this.birthDateMapper = birthDateMapper;
    this.emailMapper = emailMapper;
    this.phoneCollectionMapper = phoneCollectionMapper;
    this.propertiesMapper = propertiesMapper;
  }

  @Override
  public PersonEntity toEntity(Person domain) {
    return new PersonEntity(
        idMapper.toEntity(domain.id()),
        nameMapper.toEntity(domain.name()),
        birthDateMapper.toEntity(domain.birthDate()),
        emailMapper.toEntity(domain.email()),
        phoneCollectionMapper.toEntity(domain.phones()),
        propertiesMapper.toEntity(domain.properties()));
  }

  @Override
  public Person toDomain(PersonEntity entity) {
    return new Person(
        idMapper.toDomain(entity.getId()),
        nameMapper.toDomain(entity.getName()),
        birthDateMapper.toDomain(entity.getBirthDate()),
        emailMapper.toDomain(entity.getEmail()),
        phoneCollectionMapper.toDomain(entity.getPhones()),
        propertiesMapper.toDomain(entity.getProperties()));
  }
}
