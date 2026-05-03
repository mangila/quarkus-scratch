package com.github.mangila.crud1.web;

import com.github.mangila.crud1.shared.UuidFactory;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import java.util.UUID;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.slf4j.MDC;

/** Just a simple trace filter, OTEL is better. But for this use case it's enough. */
@Provider
public class TraceWebFilter {

  public static final String TRACE_ID_HEADER = "X-TRACE-ID";
  public static final String TRACE_ID_KEY = "traceId";

  private final UuidFactory uuidFactory;

  public TraceWebFilter(UuidFactory uuidFactory) {
    this.uuidFactory = uuidFactory;
  }

  @ServerRequestFilter(preMatching = true)
  public void filterRequest(ContainerRequestContext requestContext) {
    final UUID traceId = uuidFactory.create();
    MDC.put(TRACE_ID_KEY, traceId.toString());
  }

  @ServerResponseFilter
  public void filterResponse(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
    responseContext.getHeaders().add(TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY));
    MDC.remove(TRACE_ID_KEY);
  }
}
