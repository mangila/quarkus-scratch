package com.github.mangila.integration.jobrunr.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.integration.jobrunr.JobRunrService;
import com.github.mangila.shared.UuidFactory;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("api/v1/jobrunr")
public class JobRunrRestResource {

    private final UuidFactory uuidFactory;
    private final JobRunrService jobRunrService;

    public JobRunrRestResource(UuidFactory uuidFactory,
                               JobRunrService jobRunrService) {
        this.uuidFactory = uuidFactory;
        this.jobRunrService = jobRunrService;
    }

    @GET
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public ObjectNode getJobStatus(@PathParam("id") String id) {
        final UUID uuid = uuidFactory.create(id);
        return jobRunrService.getJobStatus(uuid);
    }

}
