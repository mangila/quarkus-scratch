package com.github.mangila.customer.web;

import com.github.mangila.customer.domain.Customer;
import com.github.mangila.customer.integration.pgevent.PgEventUtils;
import com.github.mangila.customer.shared.CustomerMapper;
import com.github.mangila.customer.shared.CustomerService;
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

    private final CustomerService customerService;
    private final ProducerTemplate producerTemplate;
    private final CustomerMapper mapper;

    public CustomerRestResource(CustomerService customerService,
                                ProducerTemplate producerTemplate,
                                CustomerMapper mapper) {
        this.customerService = customerService;
        this.producerTemplate = producerTemplate;
        this.mapper = mapper;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public CustomerDto get(UUID id) {
        MDC.put("customer.id", id.toString());
        producerTemplate.sendBody(PgEventUtils.getEndpoint("customer_evict"), "hej");
        final Customer domain = customerService.findById(id);
        final CustomerDto dto = mapper.toDto(domain);
        return dto;
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
    public Response delete(UUID id) {
        MDC.put("customer.id", id.toString());
        return Response.noContent().build();
    }

}
