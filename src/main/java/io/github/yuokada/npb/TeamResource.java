package io.github.yuokada.npb;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

@Path("/v1/team")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TeamResource {

    @Inject
    TeamRepository repository;

    @Inject
    ObjectMapper mapper;

    @GET
    @Path("/{id}")
    @APIResponseSchema(value = Team.class, responseCode = "200")
    public Response getByIdCross(@PathParam("id") Integer managerId) {
        Team team = repository.findByIdOptional(managerId).orElseThrow(NotFoundException::new);
        return Response.ok(team).build();
    }

    @GET
    @APIResponseSchema(value = Team[].class, responseCode = "200")
    public Response list(@QueryParam("include_deleted") Boolean includeDeleted) {
        var teams = repository.allTeam(includeDeleted);
        return Response.ok(teams).build();
    }

}
