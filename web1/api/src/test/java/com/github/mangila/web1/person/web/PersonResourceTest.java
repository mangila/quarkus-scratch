package com.github.mangila.web1.person.web;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hamcrest.Matchers.notNullValue;

import com.github.mangila.web1.person.PersonDtoBuilder;
import com.github.mangila.web1.person.web.model.CreatePersonRequest;
import com.github.mangila.web1.person.web.model.PersonDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.net.URI;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PersonResourceTest {

  @Test
  void shouldCreateAndDelete() {
    final URI location = create("test.email123@example.com");
    final String id = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
    given()
        .when()
        .delete("api/v1/persons/{id}", id)
        .then()
        .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
        .statusCode(204);
  }

  @Test
  void shouldCreateAndUpdate() {
    final URI location = create("test.email123@example.com");
    final String id = location.getPath().substring(location.getPath().lastIndexOf('/') + 1);
    final String expectedEmail = "another.test.emai123@example.com";
    final PersonDto dto = new PersonDtoBuilder().id(id).email(expectedEmail).build();
    given()
        .when()
        .contentType(ContentType.JSON)
        .body(dto)
        .put("api/v1/persons")
        .then()
        .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
        .statusCode(204);
    final String jsonBody =
        given()
            .when()
            .get("api/v1/persons/{id}", id)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();
    assertThatJson(jsonBody)
        .isObject()
        .containsEntry("id", id)
        .containsEntry("email", expectedEmail);
  }

  @Test
  void shouldReadPage() {
    IntStream.range(1, 11)
        .forEach(
            i -> {
              final CreatePersonRequest request =
                  new PersonDtoBuilder()
                      .email("john.doe%d@example.com".formatted(i))
                      .toCreatePersonRequest();
              given()
                  .contentType(ContentType.JSON)
                  .body(request)
                  .when()
                  .post("api/v1/persons")
                  .then()
                  .statusCode(201);
            });
    final String jsonBody =
        given()
            .when()
            .param("page", 0)
            .param("size", 10)
            .get("api/v1/persons")
            .then()
            .statusCode(200)
            .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
            .extract()
            .body()
            .asString();
    assertThatJson(jsonBody).isArray().hasSize(10);
  }

  @Test
  void shouldCreateAndRead() {
    final URI location = create("test.email@example.com");
    final String jsonBody =
        given().when().get(location.toString()).then().statusCode(200).extract().body().asString();
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys("id", "name", "birthDate", "email", "phones", "properties")
        .containsEntry("name", "John Doe")
        .containsEntry("birthDate", "1993-12-10")
        .containsEntry("email", "test.email@example.com");
    assertThatJson(jsonBody).node("phones").isArray().hasSize(1);
    assertThatJson(jsonBody)
        .node("phones[0]")
        .isObject()
        .containsOnlyKeys("number", "region", "type")
        .containsEntry("number", "+46736791310")
        .containsEntry("region", "SE")
        .containsEntry("type", "MOBILE");
    assertThatJson(jsonBody).node("properties").isObject().containsEntry("city", "Stockholm");
  }

  private URI create(String email) {
    final CreatePersonRequest request = new PersonDtoBuilder().email(email).toCreatePersonRequest();
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
}
