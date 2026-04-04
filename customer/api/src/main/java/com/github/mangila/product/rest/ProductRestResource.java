package com.github.mangila.product.rest;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;

@Path("api/v1/products")
public class ProductRestResource {

    private final ProductFileServiceRestAdapter fileServiceRestAdapter;

    public ProductRestResource(ProductFileServiceRestAdapter fileServiceRestAdapter) {
        this.fileServiceRestAdapter = fileServiceRestAdapter;
    }

    @POST
    @Path("/csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public Response csv(@RestForm("csv") FileUpload file) {
        MDC.put("domain", "product");
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
