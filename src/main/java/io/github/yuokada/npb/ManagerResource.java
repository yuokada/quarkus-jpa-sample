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
import java.util.List;

@Path("/v1/manager")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ManagerResource {
    @Inject
    ManagerRepository repository;

    @GET
    @Path("/{id}")
    public Manager getById(@PathParam("id") long managerId) {
        return repository.findByIdOptional(managerId).orElseThrow(NotFoundException::new);
    }

    @GET
    public List<Manager> list(@QueryParam("include_deleted") Boolean includeDeleted) {
        List<Manager> teams = repository.allManagers(includeDeleted);
        return teams;
    }
}
