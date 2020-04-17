package rest;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import webscraper.TagCounter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import dtos.TagDTO;
import webscraper.WebScraper;

@OpenAPIDefinition(
        info = @Info(
                title = "Webscraper API",
                version = "0.1",
                description = "Simple API to scrape a sample of webpages",
                contact = @Contact(name = "Gruppe 2", email = "gruppe2@cphbusiness.dk")
        ),
        tags = {
                @Tag(name = "scrape", description = "API related to web scraping")

        },
        servers = {
                @Server(
                        description = "For Local host testing",
                        url = "http://localhost:8080/BaseStartcode"
                ),
                @Server(
                        description = "Server API",
                        url = "https://www.paepke.software/BaseStartcode"
                )

        }
)
@Path("scrape")
public class WebScraperResource {

    @Context
    private UriInfo context;

    @Operation(summary = "Scraping a list of webpages for DIV and BODY tags",
            tags = {"scrape"},
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TagDTO.class))),
                    @ApiResponse(responseCode = "200", description = "The requested webscraping"),
                    @ApiResponse(responseCode = "403", description = "Not authenticated - do login")})
    @GET
    @RolesAllowed({"admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getTagsFast() throws InterruptedException {
        return makeResponse();
    }

    private String makeResponse() throws InterruptedException {
        List<TagCounter> dataFetched = WebScraper.runParallel();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<TagDTO> results = new ArrayList();
        for(TagCounter tc: dataFetched){
            results.add(new TagDTO(tc));
        }
        return gson.toJson(results);
    }
}
