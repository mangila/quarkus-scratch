package com.github.mangila.customer.web;

import com.github.mangila.integration.pgevent.PgEventProducer;
import com.github.mangila.customer.web.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.web.cqrs.UpdateCustomerCommand;
import com.github.mangila.customer.web.dto.CustomerDto;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.MDC;

import java.net.URI;
import java.util.UUID;

@Path("api/v1/customers")
public class CustomerRestResource {

    private final PgEventProducer pgEventProducer;
    private final CustomerServiceRestAdapter restAdapter;

    public CustomerRestResource(PgEventProducer pgEventProducer,
                                CustomerServiceRestAdapter restAdapter) {
        this.pgEventProducer = pgEventProducer;
        this.restAdapter = restAdapter;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public CustomerDto get(@PathParam("id") UUID id) {
        MDC.put("customer.id", id.toString());
        pgEventProducer.sendBody("customer_evict", null);
        return restAdapter.findById(id);
    }

    @POST
    @RunOnVirtualThread
    public Response create(@Valid CreateCustomerCommand command, @Context UriInfo uriInfo) {
        final UUID id = restAdapter.create(command);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(id.toString())
                .build();
        return Response.created(location)
                .build();
    }

    @PUT
    @RunOnVirtualThread
    public Response update(@Valid UpdateCustomerCommand command) {
        MDC.put("customer.id", command.id().toString());
        restAdapter.update(command);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RunOnVirtualThread
    public Response delete(@PathParam("id") UUID id) {
        MDC.put("customer.id", id.toString());
        restAdapter.delete(id);
        return Response.noContent().build();
    }

}
