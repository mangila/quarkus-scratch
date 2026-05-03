package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestProfile(GreetingResourceProfileTest.GreetingProfile.class)
public class GreetingResourceProfileTest {

    public static class GreetingProfile implements io.quarkus.test.junit.QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("greeting.message", "Hello from Profile!");
        }
    }

    @Test
    void testHelloEndpoint() {
        // This will still return "Hello!" because GreetingService is hardcoded.
        // But it demonstrates how to use TestProfile.
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("Hello!"));
    }
}
