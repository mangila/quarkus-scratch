package com.github.mangila.customer;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("api/v1/customer")
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        return Map.of("message", "Hello from Quarkus REST");
    }

}
