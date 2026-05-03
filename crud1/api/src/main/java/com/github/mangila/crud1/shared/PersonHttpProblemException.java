package com.github.mangila.crud1.shared;

import io.quarkiverse.resteasy.problem.HttpProblem;
import jakarta.ws.rs.core.Response;

import java.net.URI;

public class PersonHttpProblemException extends HttpProblem {

    public PersonHttpProblemException(String message, Response.Status status) {
        super(defaultMessage(message, status));
    }

    private static Builder defaultMessage(String message, Response.Status status) {
        final String title = status.getReasonPhrase();
        final int statusCode = status.getStatusCode();
        return builder()
                .withType(URI.create("about:blank"))
                .withTitle(title)
                .withDetail(message)
                .withStatus(statusCode);
    }
}
