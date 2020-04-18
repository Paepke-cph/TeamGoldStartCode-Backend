/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JokeResourceTest extends BaseResourceTest {
    @Test
    public void testGetJokes_without_login() {
        given()
                .contentType(ContentType.JSON)
                .get("jokes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode())
                .body("message", equalTo("Not authenticated - do login"));
    }

    @Disabled
    @Test
    public void testGetJokes_with_user_login() {
        String loginPayload ="{\"username\":\""+testProps.getProperty("user1_username")+"\",\"password\":\""+testProps.getProperty("user1_password")+"\"}";
        String token = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .post("login")
                .then()
                .extract()
                .path("token");
        given()
                .contentType(ContentType.JSON)
                .header("x-access-token",token)
                .get("jokes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("joke1", notNullValue(), "joke1Reference", notNullValue(),
                        "joke2",notNullValue(), "joke2Reference", notNullValue());
    }

    @Disabled
    @Test
    public void testGetJokes_with_admin_login() {
        String loginPayload ="{\"username\":\""+testProps.getProperty("user2_username")+"\",\"password\":\""+testProps.getProperty("user2_password")+"\"}";
        String token = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .post("login")
                .then()
                .extract()
                .path("token");
        given()
                .contentType(ContentType.JSON)
                .header("x-access-token",token)
                .get("jokes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("joke1", notNullValue(), "joke1Reference", notNullValue(),
                        "joke2",notNullValue(), "joke2Reference", notNullValue());
    }
}