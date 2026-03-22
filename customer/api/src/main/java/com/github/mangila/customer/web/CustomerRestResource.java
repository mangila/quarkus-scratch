package com.github.mangila.customer.web;

import com.github.mangila.customer.shared.CustomerService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("api/v1/customer")
public class CustomerRestResource {

    private final CustomerService customerService;

    public CustomerRestResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        return Map.of("message", "Hello from Quarkus REST");
    }

}
