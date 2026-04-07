package org.acme;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(GreetingResource.class)
public class GreetingResourceEndpointTest {

    @Test
    void testHelloEndpoint() {
        given()
                .when().get()
                .then()
                .statusCode(200)
                .body(is("Hello!"));
    }

    @Test
    void testGreetingEndpoint() {
        given()
                .when().get("/greeting")
                .then()
                .statusCode(200)
                .body(is("Greetings!"));
    }

}
