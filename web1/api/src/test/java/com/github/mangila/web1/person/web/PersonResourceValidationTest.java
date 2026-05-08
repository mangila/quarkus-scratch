package com.github.mangila.web1.person.web;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import com.github.mangila.web1.ResourceUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class PersonResourceValidationTest {

  private static final int STATUS_CODE = 422;
  private static final String TITLE_MESSAGE = "Constraint violation";
  private static final String INSTANCE_KEY = "instance";
  private static final String STATUS_KEY = "status";
  private static final String TITLE_KEY = "title";
  private static final String TRACE_ID_KEY = "traceId";
  private static final String VIOLATIONS_KEY = "violations";
  private static final String[] PROBLEM_KEYS = {
    INSTANCE_KEY, STATUS_KEY, TITLE_KEY, TRACE_ID_KEY, VIOLATIONS_KEY
  };
  private static final String VIOLATIONS_FIELD_KEY = "field";
  private static final String VIOLATIONS_IN_KEY = "in";
  private static final String VIOLATIONS_MESSAGE_KEY = "message";

  @Test
  void shouldReturnProblemJson() {
    final var jsonBody =
        given()
            .when()
            .get("api/v1")
            .then()
            .statusCode(404)
            .header(HttpHeaders.CONTENT_TYPE, Matchers.equalTo("application/problem+json"))
            .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue())
            .extract()
            .body()
            .asString();
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys(INSTANCE_KEY, "detail", STATUS_KEY, TITLE_KEY, TRACE_ID_KEY)
        .containsEntry(STATUS_KEY, 404)
        .containsEntry(TITLE_KEY, "Not Found")
        .containsEntry(INSTANCE_KEY, "/api/v1")
        .containsEntry("detail", "HTTP 404 Not Found");
  }

  @Test
  void shouldReturnProblemJsonWithViolations() {
    final String id = "not an uuid";
    final var jsonBody =
        given()
            .when()
            .pathParam("id", id)
            .get("api/v1/persons/{id}")
            .then()
            .statusCode(STATUS_CODE)
            .header(HttpHeaders.CONTENT_TYPE, Matchers.equalTo("application/problem+json"))
            .header(TraceWebFilter.TRACE_ID_HEADER, Matchers.notNullValue())
            .extract()
            .body()
            .asString();
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys(PROBLEM_KEYS)
        .containsEntry(STATUS_KEY, STATUS_CODE)
        .containsEntry(TITLE_KEY, TITLE_MESSAGE);
  }

  @Nested
  @DisplayName("GET: api/v1/persons/{id}")
  class FindById {

    @Test
    void shouldValidateUuidPath() {
      final String id = "not an uuid";
      final var jsonBody =
          given()
              .when()
              .pathParam("id", id)
              .get("api/v1/persons/{id}")
              .then()
              .extract()
              .body()
              .asString();
      assertThatJson(jsonBody).node(VIOLATIONS_KEY).isArray().hasSize(1);
      assertThatJson(jsonBody)
          .node(VIOLATIONS_KEY.concat("[0]"))
          .isObject()
          .containsEntry(VIOLATIONS_FIELD_KEY, "id")
          .containsEntry(VIOLATIONS_IN_KEY, "path")
          .containsEntry(VIOLATIONS_MESSAGE_KEY, "must be a valid UUID");
    }
  }

  @Nested
  @DisplayName("GET: api/v1/persons")
  class FindPage {

    @ParameterizedTest
    @ValueSource(ints = {-1})
    void testNotValidPageNumber(int pageNumber) {
      final String jsonBody =
          given()
              .when()
              .queryParam("page", pageNumber)
              .queryParam("size", 20)
              .get("api/v1/persons")
              .then()
              .extract()
              .body()
              .asString();
      assertThatJson(jsonBody).node(VIOLATIONS_KEY).isArray().hasSize(1);
      assertThatJson(jsonBody)
          .node(VIOLATIONS_KEY.concat("[0]"))
          .isObject()
          .containsEntry(VIOLATIONS_FIELD_KEY, "page")
          .containsEntry(VIOLATIONS_IN_KEY, "query")
          .containsEntry(VIOLATIONS_MESSAGE_KEY, "must be greater than or equal to 0");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 51})
    void testNotValidPageSizes(int pageSize) {
      final String jsonBody =
          given()
              .when()
              .queryParam("page", 1)
              .queryParam("size", pageSize)
              .get("api/v1/persons")
              .then()
              .extract()
              .body()
              .asString();
      assertThatJson(jsonBody).node(VIOLATIONS_KEY).isArray().hasSize(1);
      assertThatJson(jsonBody)
          .node(VIOLATIONS_KEY.concat("[0]"))
          .isObject()
          .containsEntry(VIOLATIONS_FIELD_KEY, "size")
          .containsEntry(VIOLATIONS_IN_KEY, "query");
    }
  }

  @Nested
  @DisplayName("POST: api/v1/persons")
  class Create {

    @ParameterizedTest
    @ValueSource(
        strings = {
          "data/validation/person-create-empty-name.json",
          "data/validation/person-create-empty-phones-number.json",
          "data/validation/person-create-empty-phones-region.json",
          "data/validation/person-create-empty-phones-type.json",
          "data/validation/person-create-future-birthdate.json",
          "data/validation/person-create-invalid-email.json",
          "data/validation/person-create-null-phones.json",
          "data/validation/person-create-null-properties.json",
        })
    void shouldValidateCreatePersonRequest(String resourceName) {
      final String body = ResourceUtils.getTestResource(resourceName);
      final String jsonBody =
          given()
              .body(body)
              .contentType(ContentType.JSON)
              .when()
              .post("api/v1/persons")
              .then()
              .extract()
              .body()
              .asString();
      assertThatJson(jsonBody).node(VIOLATIONS_KEY).isArray();
      assertThatJson(jsonBody)
          .node(VIOLATIONS_KEY.concat("[0]"))
          .isObject()
          .containsEntry(VIOLATIONS_IN_KEY, "body");
    }
  }

  @Nested
  @DisplayName("PUT: api/v1/persons")
  class Update {

    @ParameterizedTest
    @ValueSource(
        strings = {
          "data/validation/person-dto-invalid-uuid.json",
          "data/validation/person-dto-name-too-long.json",
          "data/validation/person-dto-name-too-short.json",
        })
    void shouldValidatePersonDto(String resourceName) {
      final String body = ResourceUtils.getTestResource(resourceName);
      final String jsonBody =
          given()
              .body(body)
              .contentType(ContentType.JSON)
              .when()
              .put("api/v1/persons")
              .then()
              .extract()
              .body()
              .asString();
      assertThatJson(jsonBody).node(VIOLATIONS_KEY).isArray();
      assertThatJson(jsonBody)
          .node(VIOLATIONS_KEY.concat("[0]"))
          .isObject()
          .containsEntry(VIOLATIONS_IN_KEY, "body");
    }
  }

  @Nested
  @DisplayName("DELETE: api/v1/persons/{id}")
  class Delete {
    @Test
    void shouldValidateUuidPath() {
      final String id = "not an uuid";
      final var jsonBody =
          given()
              .when()
              .pathParam("id", id)
              .delete("api/v1/persons/{id}")
              .then()
              .extract()
              .body()
              .asString();
      assertThatJson(jsonBody).node(VIOLATIONS_KEY).isArray().hasSize(1);
      assertThatJson(jsonBody)
          .node(VIOLATIONS_KEY.concat("[0]"))
          .isObject()
          .containsEntry(VIOLATIONS_FIELD_KEY, "id")
          .containsEntry(VIOLATIONS_IN_KEY, "path")
          .containsEntry(VIOLATIONS_MESSAGE_KEY, "must be a valid UUID");
    }
  }
}
