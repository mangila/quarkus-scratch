package com.github.mangila.web1.person.web;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import com.github.mangila.web1.TestResourcesUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.jboss.resteasy.reactive.RestResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class PersonResourceValidationTest {

  private static final String INSTANCE_KEY = "instance";
  private static final String TYPE_KEY = "type";
  private static final String DETAIL_KEY = "detail";
  private static final String STATUS_KEY = "status";
  private static final String TITLE_KEY = "title";
  private static final String TRACE_ID_KEY = "traceId";
  private static final String[] HTTP_PROBLEM_JSON_KEYS = {
    INSTANCE_KEY, DETAIL_KEY, STATUS_KEY, TITLE_KEY, TRACE_ID_KEY
  };

  private static final String VIOLATIONS_TITLE_MESSAGE = "Constraint violation";
  private static final String VIOLATIONS_KEY = "violations";
  private static final String VIOLATIONS_FIELD_KEY = "field";
  private static final String VIOLATIONS_IN_KEY = "in";
  private static final String VIOLATIONS_MESSAGE_KEY = "message";

  private static final String[] VIOLATION_PROBLEM_JSON_KEYS = {
    INSTANCE_KEY, STATUS_KEY, TITLE_KEY, TRACE_ID_KEY, VIOLATIONS_KEY
  };

  @Test
  void shouldReturn404OnNotFoundUrl() {
    final var jsonBody =
        given()
            .when()
            .get("url-not-found")
            .then()
            .statusCode(RestResponse.StatusCode.NOT_FOUND)
            .header(HttpHeaders.CONTENT_TYPE, Matchers.equalTo("application/problem+json"))
            .extract()
            .body()
            .asString();
    assertThatJson(jsonBody)
        .isObject()
        .containsOnlyKeys(HTTP_PROBLEM_JSON_KEYS)
        .containsEntry(STATUS_KEY, 404)
        .containsEntry(TITLE_KEY, "Not Found")
        .containsEntry(INSTANCE_KEY, "/url-not-found")
        .containsEntry(DETAIL_KEY, "HTTP 404 Not Found");
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
          "data/validation/person-create-name-too-short.json",
          "data/validation/person-create-name-too-long.json",
          "data/validation/person-create-empty-phones-number.json",
          "data/validation/person-create-phone-number-too-short.json",
          "data/validation/person-create-phone-number-too-long.json",
          "data/validation/person-create-empty-phones-region.json",
          "data/validation/person-create-phone-region-too-short.json",
          "data/validation/person-create-phone-region-too-long.json",
          "data/validation/person-create-empty-phones-type.json",
          "data/validation/person-create-future-birthdate.json",
          "data/validation/person-create-invalid-email.json",
          "data/validation/person-create-null-phones.json",
          "data/validation/person-create-null-properties.json",
          "data/validation/person-create-null-name.json",
          "data/validation/person-create-null-email.json",
        })
    void shouldValidatePersonCreateRequest(String resourceName) {
      final String body = TestResourcesUtils.getTestResource(resourceName);
      final String jsonBody =
          given()
              .contentType(ContentType.JSON)
              .body(body)
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
          "data/validation/person-dto-invalid-email.json",
          "data/validation/person-dto-future-birthdate.json",
          "data/validation/person-dto-empty-name.json",
          "data/validation/person-dto-phone-number-too-short.json",
          "data/validation/person-dto-phone-region-too-long.json",
          "data/validation/person-dto-empty-phones-type.json",
          "data/validation/person-dto-null-phones.json",
          "data/validation/person-dto-null-properties.json",
        })
    void shouldValidatePersonDto(String resourceName) {
      final String body = TestResourcesUtils.getTestResource(resourceName);
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
