package com.github.mangila.crud1.person.web;

import com.github.mangila.crud1.person.web.model.CreatePersonRequest;
import com.github.mangila.crud1.person.web.model.PersonDto;
import io.quarkus.panache.common.Page;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import org.hibernate.validator.constraints.UUID;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/api/v1/persons")
public class PersonResource {

  private final PersonRestService personRestService;

  public PersonResource(PersonRestService personRestService) {
    this.personRestService = personRestService;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RunOnVirtualThread
  public RestResponse<List<PersonDto>> findPage(
      @QueryParam("page") @DefaultValue("0") @PositiveOrZero int pageIndex,
      @QueryParam("size") @DefaultValue("20") @Positive @Max(50) int pageSize) {
    final Page page = Page.of(pageIndex, pageSize);
    final List<PersonDto> dtos = personRestService.findPage(page);
    return RestResponse.ok(dtos);
  }

  @Path("/{id}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RunOnVirtualThread
  public RestResponse<PersonDto> findById(@PathParam("id") @UUID String id) {
    final PersonDto dto = personRestService.findById(id);
    return RestResponse.ok(dto);
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @RunOnVirtualThread
  public RestResponse<Void> create(@Valid CreatePersonRequest request, @Context UriInfo uriInfo) {
    final java.util.UUID id = personRestService.create(request);
    final URI location = uriInfo.getAbsolutePathBuilder().path(id.toString()).build();
    return RestResponse.created(location);
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @RunOnVirtualThread
  public RestResponse<Void> update(@Valid PersonDto dto) {
    personRestService.update(dto);
    return RestResponse.noContent();
  }

  @Path("/{id}")
  @DELETE
  @RunOnVirtualThread
  public RestResponse<Void> delete(@PathParam("id") @UUID String id) {
    personRestService.delete(id);
    return RestResponse.noContent();
  }
}
