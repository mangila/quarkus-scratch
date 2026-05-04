package com.github.mangila.crud1.web;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class PersonResourceValidationTest {

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
          .statusCode(400)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(400))
          .body("title", Matchers.equalTo("Bad Request"))
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
      int pageSize = -1;
      given()
          .when()
          .queryParam("page", 1)
          .queryParam("size", pageSize)
          .get("api/v1/persons")
          .then()
          .statusCode(400)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(400))
          .body("title", Matchers.equalTo("Bad Request"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
    }

    @Test
    void pageSizeMax50() {
      int pageSize = 51;
      given()
          .when()
          .queryParam("page", 1)
          .queryParam("size", pageSize)
          .get("api/v1/persons")
          .then()
          .statusCode(400)
          .header("X-TRACE-ID", Matchers.notNullValue())
          .body("status", Matchers.equalTo(400))
          .body("title", Matchers.equalTo("Bad Request"))
          .body("traceId", Matchers.notNullValue())
          .body("violations", Matchers.hasSize(1));
    }
  }

  @Nested
  @DisplayName("Create validations")
  class Create {
    @Test
    void shouldValidatePersonDto() {}
  }

  @Nested
  @DisplayName("Update validations")
  class Update {
    @Test
    void shouldValidatePersonDto() {}
  }

  @Nested
  @DisplayName("Delete validations")
  class Delete {
    @Test
    void shouldValidateUuidPath() {}
  }
}
