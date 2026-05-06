package com.github.mangila.crud1.person.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mangila.crud1.TestResourceUtils;
import com.github.mangila.crud1.person.TestPersonBuilder;
import com.github.mangila.crud1.person.domain.Person;
import com.github.mangila.crud1.person.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.person.web.model.CreatePersonRequest;
import com.github.mangila.crud1.person.web.model.PersonDto;
import com.github.mangila.crud1.shared.UuidFactory;
import io.quarkus.jackson.runtime.ObjectMapperProducer;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusComponentTest(value = {UuidFactory.class, ObjectMapperProducer.class})
class PersonRestMapperTest {

  @Inject PersonRestMapper mapper;

  @Test
  void testToDto() {
    final Person person = TestPersonBuilder.defaultBuild();
    final PersonDto dto = mapper.toDto(person);
    assertThat(dto)
        .isNotNull()
        .hasOnlyFields("id", "name", "birthDate", "email", "phone", "properties");
  }

  @Test
  void testToDomainPerson() {
    final PersonDto dto =
        TestResourceUtils.getTestResourceAs("data/person-dto.json", PersonDto.class);
    final Person person = mapper.toDomain(dto);
    assertThat(person)
        .isNotNull()
        .hasOnlyFields("id", "name", "birthDate", "email", "phone", "properties");
  }

  @Test
  void testToDomainCreateRequest() {
    final CreatePersonRequest request =
        TestResourceUtils.getTestResourceAs(
            "data/person-create-request.json", CreatePersonRequest.class);
    final CreatePersonCommand command = mapper.toDomain(request);
    assertThat(command)
        .isNotNull()
        .hasOnlyFields("name", "birthDate", "email", "phone", "properties");
  }
}
