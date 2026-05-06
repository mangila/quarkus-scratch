package com.github.mangila.crud1.person.web;

import static io.restassured.RestAssured.given;

import com.github.mangila.crud1.TestResourceUtils;
import com.github.mangila.crud1.person.web.model.PersonDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import org.hamcrest.Matchers;
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
    final String body = TestResourceUtils.getTestResource("data/person-create-request.json");
    var location =
        given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
            .post("api/v1/persons")
            .then()
            .statusCode(201)
            .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue())
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
        .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue())
        .body("id", Matchers.notNullValue())
        .body("name", Matchers.equalTo("John Doe"))
        .body("birthDate", Matchers.equalTo("1994-10-12"))
        .body("email", Matchers.equalTo("john.doe@example.com"))
        .body("phone", Matchers.equalTo("+123456789"))
        .body("properties.city", Matchers.equalTo("Stockholm"))
        .extract()
        .body()
        .jsonPath()
        .get("id");
  }

  private void update(String id) {
    var request =
        new PersonDto(
            id,
            "John Updated",
            LocalDate.of(1994, 10, 12),
            "john.updated@example.com",
            "+987654321",
            Map.of("city", "Gothenburg"));
    given()
        .contentType(ContentType.JSON)
        .body(request)
        .when()
        .put("api/v1/persons")
        .then()
        .statusCode(204)
        .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue());

    given()
        .when()
        .get("api/v1/persons/{id}", id)
        .then()
        .statusCode(200)
        .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue())
        .body("id", Matchers.equalTo(id))
        .body("name", Matchers.equalTo("John Updated"))
        .body("birthDate", Matchers.equalTo("1994-10-12"))
        .body("email", Matchers.equalTo("john.updated@example.com"))
        .body("phone", Matchers.equalTo("+987654321"))
        .body("properties.city", Matchers.equalTo("Gothenburg"));
  }

  private void delete(String id) {
    given()
        .when()
        .delete("api/v1/persons/{id}", id)
        .then()
        .statusCode(204)
        .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue());

    given()
        .when()
        .delete("api/v1/persons/{id}", id)
        .then()
        .statusCode(404)
        .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue());

    given()
        .when()
        .get("api/v1/persons/{id}", id)
        .then()
        .statusCode(404)
        .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue());
  }
}
