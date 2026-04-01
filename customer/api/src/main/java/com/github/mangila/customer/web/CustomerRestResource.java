package com.github.mangila.customer.web;

import com.github.mangila.customer.integration.pgevent.PgEventUtils;
import com.github.mangila.customer.web.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.web.dto.CustomerDto;
import com.github.mangila.customer.web.cqrs.UpdateCustomerCommand;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;

import java.net.URI;
import java.util.UUID;

@Path("api/v1/customers")
public class CustomerRestResource {

    private final CustomerServiceRestAdapter restAdapter;
    private final ProducerTemplate producerTemplate;

    public CustomerRestResource(CustomerServiceRestAdapter restAdapter,
                                ProducerTemplate producerTemplate) {
        this.restAdapter = restAdapter;
        this.producerTemplate = producerTemplate;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public CustomerDto get(@PathParam("id") UUID id) {
        MDC.put("customer.id", id.toString());
        producerTemplate.sendBody(PgEventUtils.getEndpoint("customer_evict"), "hej");
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
