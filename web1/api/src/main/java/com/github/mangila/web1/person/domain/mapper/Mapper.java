package com.github.mangila.web1.person.domain.mapper;

public sealed interface Mapper<Entity, Domain>
    permits BirthDateMapper,
        EmailMapper,
        IdMapper,
        NameMapper,
        PersonMapper,
        PhoneCollectionMapper,
        PropertiesMapper {

  Entity toEntity(Domain domain);

  Domain toDomain(Entity entity);
}
