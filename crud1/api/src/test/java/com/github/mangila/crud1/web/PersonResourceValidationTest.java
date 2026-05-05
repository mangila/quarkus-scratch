package com.github.mangila.crud1.web;

import static io.restassured.RestAssured.given;

import com.github.mangila.crud1.TestResourceUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
public class PersonResourceValidationTest {

  private static final int STATUS_CODE = 422;

  @Nested
  @DisplayName("Find by id validations")
  class FindById {

    @Test
    void shouldValidateUuidPath() {
      final String id = "not an uuid";
      given()
          .when()
          .pathParam("id", id)
          .get("api/v1/persons/{id}")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
    }
  }

  @Nested
  @DisplayName("Find page validations")
  class FindPage {
    @Test
    void pageNumberMustBePositiveOrZero() {
      int pageNumber = -1;
      given()
          .when()
          .queryParam("page", pageNumber)
          .queryParam("size", 10)
          .get("api/v1/persons")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
      pageNumber = 0;
      given()
          .when()
          .queryParam("page", pageNumber)
          .queryParam("size", 10)
          .get("api/v1/persons")
          .then()
          .statusCode(200)
          .header("X-TRACE-ID", Matchers.notNullValue());
    }

    @Test
    void pageSizeMustBePositive() {
      final int pageSize = -1;
      given()
          .when()
          .queryParam("page", 1)
          .queryParam("size", pageSize)
          .get("api/v1/persons")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
    }

    @Test
    void pageSizeMax50() {
      final int pageSize = 51;
      given()
          .when()
          .queryParam("page", 1)
          .queryParam("size", pageSize)
          .get("api/v1/persons")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
    }
  }

  @Nested
  @DisplayName("Create validations")
  class Create {

    @ParameterizedTest
    @ValueSource(
        strings = {
          "data/validation/person-create-empty.json",
          "data/validation/person-create-future-birthdate.json",
          "data/validation/person-create-invalid.json",
          "data/validation/person-create-invalid-email.json",
          "data/validation/person-create-null-properties.json",
        })
    void shouldValidateCreatePersonRequest(String resourceName) {
      final String body = TestResourceUtils.getTestResource(resourceName);
      given()
          .body(body)
          .contentType(ContentType.JSON)
          .when()
          .post("api/v1/persons")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue());
    }
  }

  @Nested
  @DisplayName("Update validations")
  class Update {

    @ParameterizedTest
    @ValueSource(
        strings = {
          "data/validation/person-dto-invalid.json",
          "data/validation/person-dto-invalid-uuid.json",
          "data/validation/person-dto-name-too-long.json",
          "data/validation/person-dto-name-too-short.json",
        })
    void shouldValidatePersonDto(String resourceName) {
      final String body = TestResourceUtils.getTestResource(resourceName);
      given()
          .body(body)
          .contentType(ContentType.JSON)
          .when()
          .put("api/v1/persons")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue());
    }
  }

  @Nested
  @DisplayName("Delete validations")
  class Delete {
    @Test
    void shouldValidateUuidPath() {
      final String id = "not an uuid";
      given()
          .when()
          .pathParam("id", id)
          .delete("api/v1/persons/{id}")
          .then()
          .statusCode(STATUS_CODE)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(STATUS_CODE))
          .body("title", Matchers.equalTo("Constraint violation"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
    }
  }
}
