package com.github.mangila.customer.integration.pgevent;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class CacheEvictRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("pgevent:///postgres/pokemon_evict?datasource=#pgevent")
                .log("evict: ${body}");
    }
}
