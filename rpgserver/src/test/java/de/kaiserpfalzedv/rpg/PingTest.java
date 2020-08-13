package de.kaiserpfalzedv.rpg;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PingTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .auth().basic("api","abc123")
            .when()
                .get("/api/ping")
            .then()
                .statusCode(200)
                .body(is("pong"));
    }

}