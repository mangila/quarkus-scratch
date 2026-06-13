package com.github.mangila.web1.person.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mangila.web1.person.PersonBuilder;
import com.github.mangila.web1.person.PersonEntityBuilder;
import com.github.mangila.web1.person.data.PersonEntity;
import com.github.mangila.web1.person.domain.mapper.*;
import com.github.mangila.web1.shared.ObjectMapperService;
import io.quarkus.jackson.runtime.ObjectMapperProducer;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusComponentTest(
    value = {
      IdMapper.class,
      NameMapper.class,
      BirthDateMapper.class,
      EmailMapper.class,
      PhoneCollectionMapper.class,
      PropertiesMapper.class,
      ObjectMapperService.class,
      ObjectMapperProducer.class
    })
class PersonMapperTest {

  @Inject PersonMapper mapper;

  @Test
  void shouldMapToEntity() {
    final Person person = new PersonBuilder().build();
    final PersonEntity entity = mapper.toEntity(person);
    assertThat(entity)
        .isNotNull()
        .hasOnlyFields(
            "id",
            "name",
            "birthDate",
            "email",
            "phones",
            "properties",
            "createdAt",
            "updatedAt",
            "version");
  }

  @Test
  void shouldMapToDomain() {
    final PersonEntity entity = new PersonEntityBuilder().build();
    final Person person = mapper.toDomain(entity);
    assertThat(person)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("id", "name", "birthDate", "email", "phones", "properties");
  }
}
