package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceMockArgumentTest {

    @InjectMock
    GreetingService greetingService;

    @Test
    void testHelloEndpointWithArgument() {
        // This is a bit contrived since GreetingService doesn't have methods with args,
        // but it shows how to use ArgumentMatchers if it had.
        // Let's assume we want to mock a specific behavior.
        Mockito.when(greetingService.hello()).thenReturn("Mocked with Matcher");

        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Mocked with Matcher"));
    }
}
