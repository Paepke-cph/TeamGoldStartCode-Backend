package rest;

import com.google.gson.Gson;
import entity.ChuckDTO;
import entity.CombinedJoke;
import entity.DadDTO;
import utils.HttpUtils;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * REST Web Service
 *
 * @author lam
 */
@Path("jokes")
public class JokeResource {
    private final String DAD_URL = "https://icanhazdadjoke.com";
    private final String CHUCK_URL = "https://api.chucknorris.io/jokes/random";

    private Gson gson = new Gson();

    @Context
    private UriInfo context;

   
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
