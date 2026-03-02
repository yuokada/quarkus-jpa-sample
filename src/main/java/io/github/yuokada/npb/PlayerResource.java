package io.github.yuokada.npb;

import io.github.yuokada.npb.model.ErrorMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Set;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

@Path("/v1/player")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PlayerResource {

    private static final Logger LOGGER = Logger.getLogger(PlayerResource.class.getName());
    private static final Set<String> SUPPORTED_POSITIONS = Set.of("P", "C", "IF", "OF");
    private static final int PLAYER_NAME_MAX_LENGTH = 64;
    private static final int MIN_UNIFORM_NUMBER = 0;
    private static final int MAX_UNIFORM_NUMBER = 999;

    @Inject
    PlayerRepository repository;

    @Inject
    TeamRepository teamRepository;

    @GET
    @Path("/{id}")
    @APIResponseSchema(value = Player.class, responseCode = "200")
    public Response getById(@PathParam("id") Integer playerId) {
        Player player = repository.findByIdOptional(playerId).orElseThrow(NotFoundException::new);
        return Response.ok(player).build();
    }

    @GET
    @APIResponseSchema(value = Player[].class, responseCode = "200")
    public Response list(
        @QueryParam("include_deleted") Boolean includeDeleted,
        @QueryParam("team_id") Integer teamId,
        @QueryParam("position") String position,
        @QueryParam("name") String name
    ) {
        var players = repository.search(includeDeleted, teamId, position, name);
        return Response.ok(players).build();
    }

    @POST
    @Transactional
    @Path("/")
    @APIResponses({
        @APIResponse(responseCode = "201"),
        @APIResponse(responseCode = "404"),
        @APIResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
        @APIResponse(responseCode = "500")
    })
    public Response create(PlayerCreateRequest request) {
        if (request == null
            || request.teamId() == null
            || request.name() == null || request.name().isBlank()
            || request.uniformNumber() == null
            || request.position() == null || request.position().isBlank()) {
            throw new BadRequestException("teamId, name, uniformNumber and position are required.");
        }

        Team team = teamRepository.findByIdOptional(request.teamId()).orElseThrow(NotFoundException::new);
        try {
            Player player = new Player();
            player.team = team;
            player.name = normalizeName(request.name());
            player.uniformNumber = normalizeUniformNumber(request.uniformNumber());
            player.position = normalizePosition(request.position());
            repository.persistAndFlush(player);
            return Response.status(Status.CREATED).entity(player).build();
        } catch (ConstraintViolationException ex) {
            return buildConstraintViolationResponse(ex);
        }
    }

    @PUT
    @Transactional
    @Path("/{id}")
    @APIResponses({
        @APIResponse(responseCode = "200"),
        @APIResponse(responseCode = "400"),
        @APIResponse(responseCode = "404"),
        @APIResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
        @APIResponse(responseCode = "500")
    })
    public Response update(@PathParam("id") Integer playerId, PlayerUpdateRequest request) {
        if (request == null) {
            throw new BadRequestException("request body is required.");
        }

        if (request.teamId() == null
            && request.name() == null
            && request.uniformNumber() == null
            && request.position() == null) {
            throw new BadRequestException("At least one updatable field is required.");
        }

        Player player = repository.findByIdOptional(playerId).orElseThrow(NotFoundException::new);

        try {
            if (request.teamId() != null) {
                player.team = teamRepository.findByIdOptional(request.teamId()).orElseThrow(NotFoundException::new);
            }

            if (request.name() != null) {
                player.name = normalizeName(request.name());
            }

            if (request.uniformNumber() != null) {
                player.uniformNumber = normalizeUniformNumber(request.uniformNumber());
            }

            if (request.position() != null) {
                player.position = normalizePosition(request.position());
            }

            repository.persistAndFlush(player);
            return Response.ok(player).build();
        } catch (ConstraintViolationException ex) {
            return buildConstraintViolationResponse(ex);
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer playerId) {
        repository.findByIdOptional(playerId).ifPresentOrElse(player -> {
            player.isActive = false;
            repository.persist(player);
        }, () -> {
            throw new NotFoundException();
        });
        return Response.status(Status.NO_CONTENT).build();
    }

    private Response buildConstraintViolationResponse(ConstraintViolationException ex) {
        LOGGER.error("Transaction failed: ", ex);
        String constraintName = ex.getConstraintName();
        if (constraintName != null && constraintName.contains("team_id") && constraintName.contains("uniform_number")) {
            return Response.status(Status.CONFLICT)
                .entity(new ErrorMessage("Uniform number must be unique in a team.", ex.getMessage()))
                .build();
        }
        throw ex;
    }

    private String normalizeName(String name) {
        String normalized = name.trim();
        if (normalized.isBlank()) {
            throw new BadRequestException("name must not be blank.");
        }

        if (normalized.length() > PLAYER_NAME_MAX_LENGTH) {
            throw new BadRequestException("name must be 64 characters or less.");
        }
        return normalized;
    }

    private Integer normalizeUniformNumber(Integer uniformNumber) {
        if (uniformNumber < MIN_UNIFORM_NUMBER || uniformNumber > MAX_UNIFORM_NUMBER) {
            throw new BadRequestException("uniformNumber must be between 0 and 999.");
        }
        return uniformNumber;
    }

    private String normalizePosition(String position) {
        String normalized = position.trim().toUpperCase();
        if (!SUPPORTED_POSITIONS.contains(normalized)) {
            throw new BadRequestException("position must be one of P, C, IF, OF.");
        }
        return normalized;
    }

    public record PlayerCreateRequest(
        Integer teamId,
        String name,
        Integer uniformNumber,
        String position
    ) {
    }

    public record PlayerUpdateRequest(
        Integer teamId,
        String name,
        Integer uniformNumber,
        String position
    ) {
    }
}
