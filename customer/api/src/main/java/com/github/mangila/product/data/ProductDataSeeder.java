package com.github.mangila.product.data;

import com.github.mangila.integration.csv.ProductCsvRoute;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
public class ProductDataSeeder {

    private final ProducerTemplate producerTemplate;

    public ProductDataSeeder(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    void onStart(@Observes StartupEvent event) {
        producerTemplate.sendBody("direct:%s".formatted(ProductCsvRoute.ROUTE_ID), null);
    }
}
