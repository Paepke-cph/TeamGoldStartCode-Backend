package rest;

import cors.CorsRequestFilter;
import cors.CorsResponseFilter;
import errorhandling.AuthenticationExceptionMapper;
import errorhandling.UserException;
import errorhandling.UserExceptionMapper;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author lam
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(OpenApiResource.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        resources.add(CorsRequestFilter.class);
        resources.add(CorsResponseFilter.class);
        resources.add(WebScraperResource.class);
        resources.add(JokeResource.class);
        resources.add(errorhandling.GenericExceptionMapper.class);
        resources.add(security.JWTAuthenticationFilter.class);
        resources.add(LoginEndpoint.class);
        resources.add(security.RolesAllowedFilter.class);
        resources.add(AuthenticationExceptionMapper.class);
        resources.add(UserExceptionMapper.class);
    }

}
