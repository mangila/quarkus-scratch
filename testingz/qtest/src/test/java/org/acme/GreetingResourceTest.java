package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello!"));
    }

    @Test
    void testGreetingEndpoint() {
        given()
                .when().get("hello/greeting")
                .then()
                .statusCode(200)
                .body(is("Greetings!"));
    }


}