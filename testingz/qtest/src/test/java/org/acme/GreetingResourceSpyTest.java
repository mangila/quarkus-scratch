package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceSpyTest {

    @InjectSpy
    GreetingService greetingService;

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello!"));
        Mockito.verify(greetingService, Mockito.times(1)).hello();
    }

    @Test
    void testGreetingEndpoint() {
        given()
                .when().get("/hello/greeting")
                .then()
                .statusCode(200)
                .body(is("Greetings!"));
        Mockito.verify(greetingService, Mockito.times(1)).greet();

    }

}
