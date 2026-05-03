package com.github.mangila.crud1.web;

import io.quarkus.panache.common.Page;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.UUID;
import org.jboss.resteasy.reactive.RestResponse;
import org.slf4j.MDC;

import java.util.List;

@Path("/api/v1/persons")
public class PersonResource {

    private final PersonRestService personRestService;

    public PersonResource(PersonRestService personRestService) {
        this.personRestService = personRestService;
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<PersonDto> findById(@PathParam("id") @UUID String id) {
        MDC.put("personId", id);
        return RestResponse.ok(personRestService.findById(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<List<PersonDto>> findPage(@QueryParam("page") @DefaultValue("0") @PositiveOrZero int pageIndex,
                                                  @QueryParam("size") @DefaultValue("20") @Positive @Max(50) int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        return RestResponse.ok(personRestService.findPage(page));
    }

}
