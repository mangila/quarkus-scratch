package com.github.mangila.shared.exception;

import com.github.mangila.shared.UuidFactory;
import io.quarkiverse.resteasy.problem.ExceptionMapperBase;
import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
@APIResponse(responseCode = "400", description = "Bad Request: server could not understand the request due to invalid syntax")
public class NotValidUuidExceptionMapper extends ExceptionMapperBase<UuidFactory.NotValidUuidException>
        implements ExceptionMapper<UuidFactory.NotValidUuidException> {
    @Override
    protected HttpProblem toProblem(UuidFactory.NotValidUuidException e) {
        return HttpProblem.valueOf(BAD_REQUEST, e.getMessage());
    }
}
