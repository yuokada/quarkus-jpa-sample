package io.github.yuokada.npb;

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

    @GET
    @Path("/{id}")
    @APIResponseSchema(value = Manager.class, responseCode = "200")
    public Response getById(@PathParam("id") Integer managerId) {
        Manager manager = repository.findByIdOptional(managerId).orElseThrow(NotFoundException::new);
        return Response.ok(manager).build();
    }

    @GET
    @APIResponseSchema(value = Manager[].class, responseCode = "200")
    public Response list(@QueryParam("include_deleted") Boolean includeDeleted) {
        List<Manager> teams = repository.listByIncludeDeleted(includeDeleted);
        return Response.ok(teams).build();
    }
}
