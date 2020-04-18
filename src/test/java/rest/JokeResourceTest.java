/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author root
 */
public class JokeResourceTest {

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
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

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