package com.github.mangila.web1.person.web;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.mangila.web1.person.PersonCreateRequestBuilder;
import com.github.mangila.web1.person.PersonDtoBuilder;
import com.github.mangila.web1.person.data.PersonDataService;
import com.github.mangila.web1.person.data.PersonEntity;
import com.github.mangila.web1.person.web.model.PersonCreateRequest;
import com.github.mangila.web1.person.web.model.PersonDto;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
class PersonResourceCrudTest {

  @InjectSpy PersonDataService dataService;

  @Inject CacheManager cacheManager;

  CaffeineCache cache;

  @BeforeEach
  void setUp() {
    this.cache =
        Mockito.spy(cacheManager.getCache("persons").orElseThrow().as(CaffeineCache.class));
  }

  @Test
  void crud() {
    final URI location = create();
    assertThat(dataService.count()).isOne();
    final String id = read(location);
    read(location);
    assertThat(cache.keySet()).hasSize(1);
    update(id);
    assertThat(cache.keySet()).isEmpty();
    delete(id);
    assertThat(cache.keySet()).isEmpty();
    Mockito.verify(dataService, Mockito.times(1)).persist(Mockito.any(PersonEntity.class));
    Mockito.verify(dataService, Mockito.times(1)).findById(Mockito.any(UUID.class));
    Mockito.verify(dataService, Mockito.times(1)).update(Mockito.any(PersonEntity.class));
    Mockito.verify(dataService, Mockito.times(1)).delete(Mockito.any(UUID.class));
  }

  private URI create() {
    final PersonCreateRequest request = new PersonCreateRequestBuilder().build();
    var location =
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("api/v1/persons")
            .then()
            .statusCode(201)
            .extract()
            .header("Location");
    return URI.create(location);
  }

  private String read(URI location) {
    return given()
        .when()
        .get(location.toString())
        .then()
        .statusCode(200)
        .extract()
        .body()
        .jsonPath()
        .getString("id");
  }

  private void update(String id) {
    final PersonDto request = new PersonDtoBuilder().id(id).build();
    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .put("api/v1/persons")
        .then()
        .statusCode(204);
  }

  private void delete(String id) {
    given().when().delete("api/v1/persons/{id}", id).then().statusCode(204);
  }
}
