package com.github.mangila.crud1.person.web.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mangila.crud1.ResourceUtils;
import com.github.mangila.crud1.person.domain.cqrs.CreatePersonCommand;
import com.github.mangila.crud1.person.web.model.CreatePersonRequest;
import com.github.mangila.crud1.shared.UuidFactory;
import io.quarkus.jackson.runtime.ObjectMapperProducer;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusComponentTest(
    value = {
      UuidFactory.class,
      NameRestMapper.class,
      BirthDateRestMapper.class,
      EmailRestMapper.class,
      PhoneCollectionRestMapper.class,
      PropertiesRestMapper.class,
      ObjectMapperProducer.class
    })
class CreatePersonRestMapperTest {

  @Inject CreatePersonRestMapper mapper;

  @Test
  void testToDomain() {
    final CreatePersonRequest request =
        ResourceUtils.getTestResourceAs(
            "data/person-create-request.json", CreatePersonRequest.class);
    final CreatePersonCommand command = mapper.toDomain(request);
    assertThat(command)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("name", "birthDate", "email", "phones", "properties");
  }

  @Test
  void testToDtoShouldThrow() {
    assertThatThrownBy(() -> mapper.toDto(null)).isInstanceOf(UnsupportedOperationException.class);
  }
}
