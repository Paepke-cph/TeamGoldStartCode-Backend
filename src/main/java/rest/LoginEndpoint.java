package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entity.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import errorhandling.AuthenticationException;
import errorhandling.GenericExceptionMapper;
import javax.persistence.EntityManagerFactory;

import errorhandling.UserException;
import facades.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import security.SharedSecret;
import utils.EMF_Creator;

@Path("login")
public class LoginEndpoint {

  public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min
  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
  public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

  @Operation(summary = "Log in to an account, given a username and a corresponding password",
    tags = {"login"},
    responses = {
    @ApiResponse(
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "200", description = "The Requested login, returning with username and a token"),
            @ApiResponse(responseCode = "403", description = "Invalid username or password! Please try again")})
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(String jsonString) throws AuthenticationException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    String username = json.get("username").getAsString();
    String password = json.get("password").getAsString();

    try {
      User user = USER_FACADE.getVerifiedUser(username, password);
      String token = createToken(username, user.getRolesAsStrings());
      JsonObject responseJson = new JsonObject();
      responseJson.addProperty("username", username);
      responseJson.addProperty("token", token);
      return Response.ok(new Gson().toJson(responseJson)).build();

    } catch (JOSEException | AuthenticationException ex) {
      if (ex instanceof AuthenticationException) {
        throw (AuthenticationException) ex;
      }
      Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new AuthenticationException("Invalid username or password! Please try again");
  }

  @Operation(summary = "Create a regular user",
          tags = {"login"},
          responses = {
                  @ApiResponse(
                          content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                  @ApiResponse(responseCode = "200", description = "Successful creation, returning with username and a token"),
                  @ApiResponse(responseCode = "406", description = UserException.IN_USE_USERNAME)})
  @POST
  @Path("/create")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(String jsonString) throws UserException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    try {
      String username = json.get("username").getAsString();
      String password = json.get("password").getAsString();
      User user = USER_FACADE.createUser(username,password);
      String token = createToken(username, user.getRolesAsStrings());
      JsonObject responseJson = new JsonObject();
      responseJson.addProperty("username", username);
      responseJson.addProperty("token", token);
      return Response.ok(new Gson().toJson(responseJson)).build();
    } catch (NullPointerException | JOSEException e) {
      throw new UserException("Could not create user");
    }
  }

  private String createToken(String userName, List<String> roles) throws JOSEException {

    StringBuilder res = new StringBuilder();
    for (String string : roles) {
      res.append(string);
      res.append(",");
    }
    String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";
    String issuer = "semesterstartcode-dat3";

    JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
    Date date = new Date();
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(userName)
            .claim("username", userName)
            .claim("roles", rolesAsString)
            .claim("issuer", issuer)
            .issueTime(date)
            .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
            .build();
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    signedJWT.sign(signer);
    return signedJWT.serialize();

  }
}
