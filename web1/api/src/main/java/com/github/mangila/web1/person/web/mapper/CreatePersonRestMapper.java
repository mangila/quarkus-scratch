package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.cqrs.PersonCreateCommand;
import com.github.mangila.web1.person.web.model.PersonCreateRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class CreatePersonRestMapper
    implements RestMapper<PersonCreateCommand, PersonCreateRequest> {

  private final NameRestMapper nameRestMapper;
  private final BirthDateRestMapper birthDateRestMapper;
  private final EmailRestMapper emailRestMapper;
  private final PhoneCollectionRestMapper phoneRestMapper;
  private final PropertiesRestMapper propertiesRestMapper;

  public CreatePersonRestMapper(
      NameRestMapper nameRestMapper,
      BirthDateRestMapper birthDateRestMapper,
      EmailRestMapper emailRestMapper,
      PhoneCollectionRestMapper phoneRestMapper,
      PropertiesRestMapper propertiesRestMapper) {
    this.nameRestMapper = nameRestMapper;
    this.birthDateRestMapper = birthDateRestMapper;
    this.emailRestMapper = emailRestMapper;
    this.phoneRestMapper = phoneRestMapper;
    this.propertiesRestMapper = propertiesRestMapper;
  }

  @Override
  public PersonCreateCommand toDomain(PersonCreateRequest personCreateRequest) {
    return new PersonCreateCommand(
        nameRestMapper.toDomain(personCreateRequest.name()),
        birthDateRestMapper.toDomain(personCreateRequest.birthDate()),
        emailRestMapper.toDomain(personCreateRequest.email()),
        phoneRestMapper.toDomain(personCreateRequest.phones()),
        propertiesRestMapper.toDomain(personCreateRequest.properties()));
  }

  @Override
  public PersonCreateRequest toDto(PersonCreateCommand personCreateCommand) {
    throw new UnsupportedOperationException();
  }
}
