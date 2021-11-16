package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testGreetings() {
        given()
                .log().ifValidationFails(LogDetail.ALL, true)
                .auth().oauth2(getExpiredToken(Set.of("greet")))
                .when().get("/api/greetings")
                .then()
                .statusCode(200)
                .body(is("""
                        [{"message":"Hello!"},{"message":"Hola!"},{"message":"Bonjour!"},{"message":"Ciao!"}]"""));
    }

    @Test
    public void testGreetingsForbidden() {
        given()
                .log().ifValidationFails(LogDetail.ALL, true)
                .auth().oauth2(getExpiredToken(Set.of()))
                .when().get("/api/greetings")
                .then()
                .statusCode(403);
    }

    @Test
    public void testGreeting() {
        given()
                .log().ifValidationFails(LogDetail.ALL, true)
                .auth().oauth2(getExpiredToken(Set.of("greet")))
                .when().get("/api/greetings/Hello!")
                .then()
                .statusCode(200)
                .body(is("""
                        {"message":"Hello!"}"""));
    }

    @Test
    public void testGreetingForbidden() {
        given()
                .log().ifValidationFails(LogDetail.ALL, true)
                .auth().oauth2(getExpiredToken(Set.of()))
                .when().get("/api/greetings/Hello!")
                .then()
                .statusCode(403);
    }

    private String getExpiredToken(Set<String> roles) {
        return Jwt.issuer("Test")
                .subject("unmUowNPQ5Q3uXxX3OEY0NtSFsNXVzJVgaVr6n41cXo")
                .claim(Claims.preferred_username, "TestUser@company.com")
                .claim(Claims.aud.name(), "f93ee1a8-ba1a-4494-bfe1-6438230225ea")
                .claim(Claims.iat.name(), OffsetDateTime.now().toEpochSecond())
                .claim(Claims.exp.name(), OffsetDateTime.now().plusHours(1).toEpochSecond())
                .claim("name", "Test User")
                .claim("roles", roles)
                .sign();
    }
}