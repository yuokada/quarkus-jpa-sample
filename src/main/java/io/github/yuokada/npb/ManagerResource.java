package io.github.yuokada.npb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

@Path("/v1/manager")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ManagerResource {

    @Inject
    ManagerRepository repository;

    @Inject
    ObjectMapper mapper;

    @GET
    @Path("/{id}")
    @APIResponseSchema(value = Manager.class, responseCode = "200")
    public Response getByIdCross(@PathParam("id") long managerId) {
        Manager manager = repository.findByIdOptional(managerId).orElseThrow(NotFoundException::new);
        return Response.ok(manager).build();
    }

    @GET
    @APIResponseSchema(value = Manager[].class, responseCode = "200")
    public Response list(@QueryParam("include_deleted") Boolean includeDeleted) {
        List<Manager> teams = repository.allManagers(includeDeleted);
        return Response.ok(teams).build();
    }

    private ManageResponse convertToManageResponse(Manager manager) {
        return new ManageResponse(
            manager.id,
            manager.name,
            manager.team.id,
            manager.team.name
        );
    }

    @JsonSerialize
    public record ManageResponse(
        Integer id,
        String name,
        Integer team_id,
        String team_name) {
    }

}
