package com.github.mangila.customer.rest;

import com.github.mangila.customer.rest.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.rest.cqrs.UpdateCustomerCommand;
import com.github.mangila.customer.rest.dto.CustomerDto;
import com.github.mangila.shared.UuidFactory;
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
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.MDC;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("api/v1/customers")
public class CustomerRestResource {

    private final UuidFactory uuidFactory;
    private final CustomerServiceRestAdapter restAdapter;

    public CustomerRestResource(UuidFactory uuidFactory,
                                CustomerServiceRestAdapter customerServiceRestAdapter) {
        this.uuidFactory = uuidFactory;
        this.restAdapter = customerServiceRestAdapter;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<List<CustomerDto>> findAll(@QueryParam("page") @DefaultValue("0") @PositiveOrZero int pageIndex,
                                                   @QueryParam("size") @DefaultValue("20") @Positive @Max(50) int pageSize) {
        MDC.put("domain", "customer");
        Page page = Page.of(pageIndex, pageSize);
        return RestResponse.ok(restAdapter.findAll(page));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<CustomerDto> get(@PathParam("id") String id) {
        final UUID uuid = uuidFactory.create(id);
        MDC.put("customer.id", uuid.toString());
        return RestResponse.ok(restAdapter.findById(uuid));
    }


    @POST
    @RunOnVirtualThread
    public RestResponse<?> create(@Valid CreateCustomerCommand command, @Context UriInfo uriInfo) {
        final UUID id = restAdapter.create(command);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(id.toString())
                .build();
        return RestResponse.created(location);
    }

    @PUT
    @RunOnVirtualThread
    public RestResponse<?> update(@Valid UpdateCustomerCommand command) {
        MDC.put("customer.id", command.id().toString());
        restAdapter.update(command);
        return RestResponse.noContent();
    }

    @DELETE
    @Path("/{id}")
    @RunOnVirtualThread
    public RestResponse<?> delete(@PathParam("id") String id) {
        final UUID uuid = uuidFactory.create(id);
        MDC.put("customer.id", uuid.toString());
        restAdapter.delete(uuid);
        return RestResponse.noContent();
    }

    @POST
    @Path("/csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public RestResponse<Map<String, UUID>> scheduleUpload(@RestForm("csv") FileUpload file) {
        MDC.put("domain", "customer");
        MDC.put("file.name", file.fileName());
        MDC.put("file.size", String.valueOf(file.size()));
        MDC.put("file.type", file.contentType());
        MDC.put("file.path", file.uploadedFile().toString());
        UUID jobId = restAdapter.scheduleUpload(file);
        return RestResponse.accepted(Map.of("jobId", jobId));
    }

    @GET
    @Path("/csv")
    @Produces(MediaType.TEXT_PLAIN)
    @RunOnVirtualThread
    public RestResponse<?> scheduleDownload() {
        MDC.put("domain", "customer");
        java.nio.file.Path path = restAdapter.scheduleDownload();
        return RestResponse.ResponseBuilder
                .ok()
                .header("Content-Disposition", "attachment; filename='%s'".formatted(path.getFileName()))
                .entity(path)
                .build();
    }
}
