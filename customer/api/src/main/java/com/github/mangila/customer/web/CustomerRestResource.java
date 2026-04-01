package com.github.mangila.customer.web;

import com.github.mangila.customer.integration.pgevent.PgEventUtils;
import com.github.mangila.customer.shared.CustomerServiceRestAdapter;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.camel.ProducerTemplate;
import org.slf4j.MDC;

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
    public Response create(@Valid CustomerDto dto) {
        return Response.ok().entity("ok").build();
    }

    @PUT
    @RunOnVirtualThread
    public Response update(@Valid CustomerDto dto) {
        return Response.ok().entity("ok").build();
    }

    @DELETE
    @Path("/{id}")
    @RunOnVirtualThread
    public Response delete(@PathParam("id") UUID id) {
        MDC.put("customer.id", id.toString());
        return Response.noContent().build();
    }

}
