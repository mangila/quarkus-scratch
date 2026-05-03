package com.github.mangila.crud1.shared;

import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.UUID;

public class PersonDomainException extends HttpProblem {

    public PersonDomainException(String message) {
        super(defaultMessage(message, Response.Status.BAD_REQUEST));
    }

    public PersonDomainException(String message, Response.Status status) {
        super(defaultMessage(message, status));
    }

    private static Builder defaultMessage(String message, Response.Status status) {
        final UUID errorId = UUID.randomUUID();
        return builder()
                .withHeader("X-ERROR-ID", errorId.toString())
                .withType(URI.create("about:blank"))
                .withTitle(status.getReasonPhrase())
                .withDetail(message)
                .withStatus(status.getStatusCode())
                .with("errorId", errorId.toString());
    }
}
