package rest;

import com.google.gson.Gson;
import dtos.TagDTO;
import dtos.ChuckDTO;
import dtos.CombinedJoke;
import dtos.DadDTO;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import utils.HttpUtils;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;


@OpenAPIDefinition(
        info = @Info(
                title = "Team Gold startcode API",
                version = "0.1",
                description = "This API is use as a base for building a backend for a separate Frontend",
                contact = @Contact(name = "Gruppe 2", email = "gruppe2@cphbusiness.dk")
        ),
        tags = {
                @Tag(name = "jokes", description = "API related to Jokes"),
                @Tag(name = "login", description = "API related to Login"),
                @Tag(name = "scrape", description = "API related to WebScraping")
        },
        servers = {
                @Server(
                        description = "For Local host testing",
                        url = "http://localhost:8080/TeamGoldStartCode"
                ),
                @Server(
                        description = "Server API",
                        url = "https://www.paepke.software/TeamGoldStartCode"
                )
        }
)
@Path("jokes")
public class JokeResource {
    private final String DAD_URL = "https://icanhazdadjoke.com";
    private final String CHUCK_URL = "https://api.chucknorris.io/jokes/random";

    private Gson gson = new Gson();
    @Context
    private UriInfo context;

    @Operation(summary = "Getting two random jokes, one Chuck Norris and one Dad Joke",
            tags = {"jokes"},
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CombinedJoke.class))),
                    @ApiResponse(responseCode = "200", description = "The requested random jokes"),
                    @ApiResponse(responseCode = "403", description = "Not authenticated - do login")})
    @GET
    @RolesAllowed({"user","admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokes() throws IOException {
        String chuckJson = HttpUtils.fetchData(CHUCK_URL);
        String dadJson = HttpUtils.fetchData(DAD_URL);
        ChuckDTO chuckDTO = gson.fromJson(chuckJson, ChuckDTO.class);
        DadDTO dadDTO = gson.fromJson(dadJson, DadDTO.class);
        dadDTO.setUrl(DAD_URL);
        return gson.toJson(new CombinedJoke(chuckDTO, dadDTO));
    }
}
