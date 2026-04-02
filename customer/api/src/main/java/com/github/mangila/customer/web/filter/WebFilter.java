package com.github.mangila.customer.web.filter;

import io.quarkus.logging.Log;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

@Provider
public class WebFilter {

    @ServerRequestFilter(preMatching = true)
    public void filterRequest(ContainerRequestContext requestContext) {
        Log.info(requestContext.getUriInfo().getRequestUri());
    }

    @ServerResponseFilter
    public void filterResponse(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        try {
            Log.info(requestContext.getUriInfo().getRequestUri());
        } finally {
            MDC.clear();
        }
    }
}
