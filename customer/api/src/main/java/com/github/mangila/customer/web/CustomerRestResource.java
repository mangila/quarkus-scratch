package com.github.mangila.customer.web;

import com.github.mangila.customer.config.ApplicationConfig;
import com.github.mangila.customer.shared.CustomerService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("api/v1/customer")
public class CustomerRestResource {

    private final ApplicationConfig applicationConfig;
    private final ApplicationConfig.IntegrationConfig integrationConfig;
    private final CustomerService customerService;

    public CustomerRestResource(ApplicationConfig applicationConfig, ApplicationConfig.IntegrationConfig integrationConfig, CustomerService customerService) {
        this.applicationConfig = applicationConfig;
        this.integrationConfig = integrationConfig;
        this.customerService = customerService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> hello() {
        final var map = Map.of("app.secret", applicationConfig.secret(),
        "integration.secret", integrationConfig.secret()
        );
        return map;
    }

}
