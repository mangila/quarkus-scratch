package com.github.mangila.web1.person.web.mapper;

public sealed interface RestMapper<Domain, Dto>
    permits BirthDateRestMapper,
        CreatePersonRestMapper,
        EmailRestMapper,
        IdRestMapper,
        NameRestMapper,
        PersonRestMapper,
        PhoneCollectionRestMapper,
        PhoneRestMapper,
        PropertiesRestMapper {

  Domain toDomain(Dto dto);

  Dto toDto(Domain domain);
}
