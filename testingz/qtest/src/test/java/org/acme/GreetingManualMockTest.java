package org.acme;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingManualMockTest {

    @BeforeAll
    public static void setup() {
        GreetingService mock = Mockito.mock(GreetingService.class);
        Mockito.when(mock.hello()).thenReturn("Manual Mock!");
        QuarkusMock.installMockForType(mock, GreetingService.class);
    }

    @Test
    void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Manual Mock!"));
    }
}
