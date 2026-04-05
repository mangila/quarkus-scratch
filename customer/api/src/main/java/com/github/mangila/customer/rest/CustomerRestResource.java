package com.github.mangila.customer.rest;

import com.github.mangila.customer.rest.cqrs.CreateCustomerCommand;
import com.github.mangila.customer.rest.cqrs.UpdateCustomerCommand;
import com.github.mangila.customer.rest.dto.CustomerDto;
import com.github.mangila.integration.pgevent.PgEventProducer;
import com.github.mangila.shared.UuidFactory;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.MDC;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Path("api/v1/customers")
public class CustomerRestResource {

    private final PgEventProducer pgEventProducer;
    private final UuidFactory uuidFactory;
    private final CustomerServiceRestAdapter restAdapter;
    private final CustomerFileServiceRestAdapter fileServiceRestAdapter;

    public CustomerRestResource(PgEventProducer pgEventProducer,
                                UuidFactory uuidFactory,
                                CustomerServiceRestAdapter customerServiceRestAdapter,
                                CustomerFileServiceRestAdapter customerFileServiceRestAdapter) {
        this.pgEventProducer = pgEventProducer;
        this.uuidFactory = uuidFactory;
        this.restAdapter = customerServiceRestAdapter;
        this.fileServiceRestAdapter = customerFileServiceRestAdapter;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public CustomerDto get(@PathParam("id") String id) {
        final UUID uuid = uuidFactory.create(id);
        MDC.put("customer.id", uuid.toString());
        return restAdapter.findById(uuid);
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
    public Response delete(@PathParam("id") String id) {
        final UUID uuid = uuidFactory.create(id);
        MDC.put("customer.id", uuid.toString());
        restAdapter.delete(uuid);
        return Response.noContent().build();
    }

    @POST
    @Path("/csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public Response csv(@RestForm("csv") FileUpload file) {
        MDC.put("domain", "customer");
        MDC.put("file.name", file.fileName());
        MDC.put("file.size", String.valueOf(file.size()));
        MDC.put("file.type", file.contentType());
        MDC.put("file.path", file.uploadedFile().toString());
        UUID jobId = fileServiceRestAdapter.upload(file);
        return Response.accepted()
                .entity(Map.of("jobId", jobId))
                .build();
    }
}
