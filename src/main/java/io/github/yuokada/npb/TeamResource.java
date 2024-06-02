package io.github.yuokada.npb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yuokada.npb.model.ErrorMessage;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.narayana.jta.QuarkusTransactionException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.RollbackException;
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
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

@Path("/v1/team")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TeamResource {

    private static final Logger LOGGER = Logger.getLogger(TeamResource.class.getName());

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

    @POST
    // @Transactional
    @Path("/")
    @APIResponses({
        @APIResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Void.class))),
        @APIResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
        @APIResponse(responseCode = "500")
    })
    public Response post(
        TeamCreateRequest request
    ) {
        try {
            QuarkusTransaction.begin();
            Team team = new Team();
            team.name = request.name;
            repository.persist(team);
            QuarkusTransaction.commit();

            return Response.status(Response.Status.CREATED).build();
        } catch (QuarkusTransactionException ex) {
            if (ex.getCause() instanceof RollbackException rollbackException) {
                if (rollbackException.getCause() instanceof ConstraintViolationException) {
                    String constraintName = ((ConstraintViolationException) rollbackException.getCause()).getConstraintName();
                    if (constraintName != null && constraintName.contains("team_name")) {
                        return Response.status(Status.CONFLICT)
                            .entity(new ErrorMessage("Team name must be unique.",
                                rollbackException.getCause().getMessage()))
                            .build();
                    }
                }
            }
            LOGGER.error("Transaction failed: ", ex);
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorMessage("Internal server error", ex.getMessage()))
                .build();
        }
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
