package io.github.yuokada.npb;

import io.github.yuokada.npb.model.ErrorMessage;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String constraintName = exception.getConstraintName();
        if (constraintName != null && constraintName.contains("team_name")) {
            return buildConflict("Team name must be unique.", exception);
        }

        if (constraintName != null
            && constraintName.contains("team_id")
            && constraintName.contains("uniform_number")) {
            return buildConflict("Uniform number must be unique in a team.", exception);
        }

        return buildConflict("Constraint violation.", exception);
    }

    private Response buildConflict(String message, ConstraintViolationException exception) {
        return Response.status(Status.CONFLICT)
            .entity(new ErrorMessage(message, exception.getMessage()))
            .build();
    }
}
