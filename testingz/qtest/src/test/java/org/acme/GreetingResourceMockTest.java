package org.acme;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceMockTest {

    @InjectMock
    GreetingService greetingService;

    @BeforeEach
    public void setup() {
        Mockito.when(greetingService.hello()).thenReturn("Hello from Mockito!");
        Mockito.when(greetingService.greet()).thenReturn("Greetings from Mockito!");
    }

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello from Mockito!"));
    }

    @Test
    void testGreetingEndpoint() {
        given()
                .when().get("/hello/greeting")
                .then()
                .statusCode(200)
                .body(is("Greetings from Mockito!"));
    }

}
