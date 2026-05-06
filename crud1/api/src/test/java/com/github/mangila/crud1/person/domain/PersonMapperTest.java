package com.github.mangila.crud1.person.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mangila.crud1.person.TestPersonBuilder;
import com.github.mangila.crud1.person.TestPersonEntityBuilder;
import com.github.mangila.crud1.person.data.PersonEntity;
import io.quarkus.test.component.QuarkusComponentTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusComponentTest
class PersonMapperTest {

  @Inject PersonMapper mapper;

  @Test
  void shouldMapToEntity() {
    final Person person = TestPersonBuilder.defaultBuild();
    final PersonEntity entity = mapper.toEntity(person);
    assertThat(entity)
        .isNotNull()
        .hasOnlyFields(
            "id",
            "name",
            "birthDate",
            "email",
            "phone",
            "properties",
            "createdAt",
            "updatedAt",
            "version");
  }

  @Test
  void shouldMapToDomain() {
    final PersonEntity entity = TestPersonEntityBuilder.defaultBuild();
    final Person person = mapper.toDomain(entity);
    assertThat(person)
        .isNotNull()
        .hasNoNullFieldsOrProperties()
        .hasOnlyFields("id", "name", "birthDate", "email", "phone", "properties");
  }
}
