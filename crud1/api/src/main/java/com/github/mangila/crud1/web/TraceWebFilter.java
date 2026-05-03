package com.github.mangila.crud1.web;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Just a simple trace filter, OTEL is better. But for this use case it's enough.
 */
@Provider
public class TraceWebFilter {

    public static final String TRACE_ID_KEY = "traceId";

    @ServerRequestFilter(preMatching = true)
    public void filterRequest(ContainerRequestContext requestContext) {
        final String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID_KEY, traceId);
    }

    @ServerResponseFilter
    public void filterResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("X-TRACE-ID", MDC.get(TRACE_ID_KEY));
        MDC.remove(TRACE_ID_KEY);
    }
}
