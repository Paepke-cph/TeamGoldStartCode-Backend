package rest;

import errorhandling.UserException;
import io.restassured.http.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginEndpointTest extends BaseResourceTest {
    @Test
    public void testLogin_with_incorrect_password() {
        String payload ="{\"username\":\"user\",\"password\":\"blablabla\"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode())
                .body("message", equalTo("Invalid user name or password"));
    }

    @Test
    public void testCreate_with_incorrect_data() {
        String payload = "{\"password\":\"blablabla\"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login/create")
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE_406.getStatusCode())
                .body("message", equalTo("Could not create user"));
    }

    @Disabled
    @Test
    public void testCreate_with_duplicate_username() {
        String payload = "{\"username\":\"user\",\"password\":\"this is actually not the real password \"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login/create")
                .then()
                .statusCode(HttpStatus.NOT_ACCEPTABLE_406.getStatusCode())
                .body("message", equalTo(UserException.IN_USE_USERNAME));
    }

    @Disabled
    @Test
    public void testLogin_with_correct_password() {
        String payload = "{\"username\":\""+testProps.getProperty("user1_username")+"\",\"password\":\""+testProps.getProperty("user1_password")+"\"}";
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post("login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("token", notNullValue());
    }

    @Test
    public void testAccess_non_existing_page() {
        String page = "logins";
        given()
                .contentType(ContentType.JSON)
                .get(page)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("HTTP 404 Not Found"));
    }
}