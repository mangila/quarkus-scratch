package com.github.mangila.web1.person.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mangila.web1.person.PersonBuilder;
import com.github.mangila.web1.person.PersonDtoBuilder;
import com.github.mangila.web1.person.domain.Person;
import com.github.mangila.web1.person.web.model.PersonDto;
import com.github.mangila.web1.shared.ObjectMapperService;
import com.github.mangila.web1.shared.UuidFactory;
import io.quarkus.jackson.runtime.ObjectMapperProducer;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusComponentTest(
    value = {
      UuidFactory.class,
      IdRestMapper.class,
      NameRestMapper.class,
      BirthDateRestMapper.class,
      EmailRestMapper.class,
      PhoneCollectionRestMapper.class,
      PhoneRestMapper.class,
      PropertiesRestMapper.class,
      ObjectMapperService.class,
      ObjectMapperProducer.class
    })
class PersonRestMapperTest {

  @Inject PersonRestMapper mapper;

  @Test
  void shouldMapToDto() {
    final Person person = new PersonBuilder().build();
    final PersonDto dto = mapper.toDto(person);
    assertThat(dto)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("id", "name", "birthDate", "email", "phones", "properties");
  }

  @Test
  void shouldMapToDomain() {
    final PersonDto dto = new PersonDtoBuilder().build();
    final Person person = mapper.toDomain(dto);
    assertThat(person)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("id", "name", "birthDate", "email", "phones", "properties");
  }
}
