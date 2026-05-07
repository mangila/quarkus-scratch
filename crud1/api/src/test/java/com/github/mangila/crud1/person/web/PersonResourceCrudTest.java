package com.github.mangila.crud1.person.web;

import static io.restassured.RestAssured.given;

import com.github.mangila.crud1.ResourceUtils;
import com.github.mangila.crud1.person.PersonDtoBuilder;
import com.github.mangila.crud1.person.web.model.PersonDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.net.URI;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PersonResourceCrudTest {

  @Test
  void crud() {
    final URI location = create();
    final String id = read(location);
    update(id);
    delete(id);
  }

  private URI create() {
    final String body = ResourceUtils.getTestResource("data/person-create-request.json");
    var location =
        given()
            .contentType(ContentType.JSON)
            .body(body)
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
    final PersonDto request = PersonDtoBuilder.defaultBuild(id);
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

    given().when().get("api/v1/persons/{id}", id).then().statusCode(404);
  }
}
