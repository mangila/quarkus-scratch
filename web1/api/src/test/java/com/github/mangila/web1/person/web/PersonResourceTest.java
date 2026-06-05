package com.github.mangila.web1.person.web;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.hamcrest.Matchers.notNullValue;

import com.github.mangila.web1.person.PersonCreateRequestBuilder;
import com.github.mangila.web1.person.PersonDtoBuilder;
import com.github.mangila.web1.person.web.model.PersonCreateRequest;
import com.github.mangila.web1.person.web.model.PersonDto;
import com.github.mangila.web1.person.web.model.PhoneDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PersonResourceTest {

  @Test
  void shouldCreateAndRead() {
    final String email = "test.create@example.com";
    final PersonCreateRequest request = new PersonCreateRequestBuilder().email(email).build();
    final String id = create(request);
    final String jsonBody = read(id);
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys("id", "name", "birthDate", "email", "phones", "properties")
        .containsEntry("name", "John Doe")
        .containsEntry("birthDate", "1993-12-10")
        .containsEntry("email", email);
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

  @Test
  void shouldCreateAndUpdate() {
    final String email = "test.update@example.com";
    final PersonCreateRequest request = new PersonCreateRequestBuilder().email(email).build();
    final String id = create(request);
    final PersonDto personDto =
        new PersonDtoBuilder()
            .id(id)
            .email("test.update2@example.com")
            .name("Jane Doe")
            .birthDate(LocalDate.of(2002, 10, 12))
            .addPhone(new PhoneDto("+46736791311", "SE", "MOBILE"))
            .addProperty("city", "Sundsvall")
            .build();
    given()
        .contentType(ContentType.JSON)
        .body(personDto)
        .when()
        .put("api/v1/persons")
        .then()
        .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
        .statusCode(RestResponse.StatusCode.NO_CONTENT);
    final String jsonBody = read(id);
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys("id", "name", "birthDate", "email", "phones", "properties")
        .containsEntry("name", "Jane Doe")
        .containsEntry("birthDate", "2002-10-12")
        .containsEntry("email", "test.update2@example.com");
    assertThatJson(jsonBody).node("phones").isArray().hasSize(2);
    assertThatJson(jsonBody)
        .node("phones[0]")
        .isObject()
        .containsOnlyKeys("number", "region", "type")
        .containsEntry("number", "+46736791310")
        .containsEntry("region", "SE")
        .containsEntry("type", "MOBILE");
    assertThatJson(jsonBody).node("properties").isObject().containsEntry("city", "Sundsvall");
  }

  @Test
  void shouldCreateAndDelete() {
    final String email = "test.delete.delete@example.com";
    final PersonCreateRequest request = new PersonCreateRequestBuilder().email(email).build();
    final String id = create(request);
    given()
        .when()
        .delete("api/v1/persons/{id}", id)
        .then()
        .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
        .statusCode(RestResponse.StatusCode.NO_CONTENT);
  }

  @Test
  void shouldReadPage() {
    IntStream.range(1, 51)
        .forEach(
            i -> {
              final PersonCreateRequest request =
                  new PersonCreateRequestBuilder()
                      .email("page.doe%d@example.com".formatted(i))
                      .build();
              final String _ = create(request);
            });
    final String jsonBody =
        given()
            .when()
            .param("page", 0)
            .param("size", 10)
            .get("api/v1/persons")
            .then()
            .statusCode(RestResponse.StatusCode.OK)
            .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
            .extract()
            .body()
            .asString();
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys("content", "totalCount", "pageCount", "hasNextPage", "hasPreviousPage")
        .containsEntry("pageCount", 5)
        .containsEntry("totalCount", 50)
        .containsEntry("hasNextPage", true)
        .containsEntry("hasPreviousPage", false)
        .node("content")
        .isArray()
        .hasSize(10);
  }

  @Test
  void shouldCreateMany() {
    final List<PersonCreateRequest> request =
        IntStream.range(1, 501)
            .mapToObj(
                i -> {
                  return new PersonCreateRequestBuilder()
                      .email("bulk.doe%d@example.com".formatted(i))
                      .build();
                })
            .toList();

    final String jsonBody =
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("api/v1/persons/bulk")
            .then()
            .statusCode(RestResponse.StatusCode.OK)
            .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
            .extract()
            .body()
            .asString();

    assertThatJson(jsonBody).isObject().containsEntry("count", 500);
  }

  private String create(PersonCreateRequest request) {
    String location =
        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("api/v1/persons")
            .then()
            .statusCode(RestResponse.StatusCode.CREATED)
            .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
            .extract()
            .header("Location");
    return location.substring(location.lastIndexOf("/") + 1);
  }

  private String read(String id) {
    return given()
        .when()
        .get("api/v1/persons/{id}", id)
        .then()
        .statusCode(RestResponse.StatusCode.OK)
        .header(TraceWebFilter.TRACE_ID_HEADER, notNullValue())
        .extract()
        .body()
        .asString();
  }
}
