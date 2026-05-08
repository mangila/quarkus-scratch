package com.github.mangila.web1.person.web.mapper;

import com.github.mangila.web1.person.domain.cqrs.CreatePersonCommand;
import com.github.mangila.web1.person.web.model.CreatePersonRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class CreatePersonRestMapper
    implements RestMapper<CreatePersonCommand, CreatePersonRequest> {

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
  public CreatePersonCommand toDomain(CreatePersonRequest createPersonRequest) {
    return new CreatePersonCommand(
        nameRestMapper.toDomain(createPersonRequest.name()),
        birthDateRestMapper.toDomain(createPersonRequest.birthDate()),
        emailRestMapper.toDomain(createPersonRequest.email()),
        phoneRestMapper.toDomain(createPersonRequest.phones()),
        propertiesRestMapper.toDomain(createPersonRequest.properties()));
  }

  @Override
  public CreatePersonRequest toDto(CreatePersonCommand createPersonCommand) {
    throw new UnsupportedOperationException();
  }
}
