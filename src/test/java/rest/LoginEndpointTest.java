package rest;

import errorhandling.UserException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.swing.text.AbstractDocument;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginEndpointTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static Properties testProps = new Properties();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() throws IOException {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
        //testProps.load(JokeResourceTest.class.getClassLoader().getResourceAsStream("testing.properties"));
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
    }

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