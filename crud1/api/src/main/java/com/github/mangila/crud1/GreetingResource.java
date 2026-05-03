package com.github.mangila.crud1;

import com.github.mangila.crud1.shared.ApplicationException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        throw new ApplicationException("Hello from Quarkus REST");
    }
}
