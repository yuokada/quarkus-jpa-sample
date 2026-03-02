package io.github.yuokada.npb;

import io.github.yuokada.npb.model.ErrorMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

@Path("/v1/team")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TeamResource {

    @Inject
    TeamRepository repository;

    @GET
    @Path("/{id}")
    @APIResponseSchema(value = Team.class, responseCode = "200")
    public Response getById(@PathParam("id") Integer teamId) {
        Team team = repository.findByIdOptional(teamId).orElseThrow(NotFoundException::new);
        return Response.ok(team).build();
    }

    @GET
    @APIResponseSchema(value = Team[].class, responseCode = "200")
    public Response list(@QueryParam("include_deleted") Boolean includeDeleted) {
        var teams = repository.listByIncludeDeleted(includeDeleted);
        return Response.ok(teams).build();
    }

    @POST
    @Transactional
    @Path("/")
    @APIResponses({
        @APIResponse(responseCode = "201"),
        @APIResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
        @APIResponse(responseCode = "500")
    })
    public Response create(TeamCreateRequest request) {
        Team team = new Team();
        team.name = request.name;
        repository.persistAndFlush(team);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    // @APIResponseSchema(value = Team.class, responseCode = "204")
    public Response delete(
        @PathParam("id") Integer id
    ) {
        repository.findByIdOptional(id).ifPresentOrElse(r -> {
                r.isActive = false;
                repository.persist(r);
            },
            () -> {
                throw new NotFoundException();
            });
        return Response.status(Status.NO_CONTENT).build();
    }

    public record TeamCreateRequest(
        String name
    ) {

    }

}
