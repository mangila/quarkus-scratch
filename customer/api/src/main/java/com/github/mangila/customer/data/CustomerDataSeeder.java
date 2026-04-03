package com.github.mangila.customer.data;

import com.github.mangila.integration.csv.CustomerCsvRoute;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
public class CustomerDataSeeder {

    private final ProducerTemplate producerTemplate;

    public CustomerDataSeeder(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    void onStart(@Observes StartupEvent event) {
        producerTemplate.sendBody("direct:%s".formatted(CustomerCsvRoute.ROUTE_ID), null);
    }
}
