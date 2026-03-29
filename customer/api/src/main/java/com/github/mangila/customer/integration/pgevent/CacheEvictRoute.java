package com.github.mangila.customer.integration.pgevent;

import com.github.mangila.customer.config.AppConfig;
import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.customer.data.CustomerRedisRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.jboss.logging.MDC;

/**
 * Listen to updates on the customer table and evict the corresponding cache entry using PostgreSQL LISTEN/NOTIFY.
 * <p>
 * If a notification is lost, we will have "dirty" data in the cache for a short time until the TTL kicks in.
 */
@ApplicationScoped
public class CacheEvictRoute extends RouteBuilder {

    private final String endpoint;

    public CacheEvictRoute(AppConfig.IntegrationConfig.PgEvent pgEventConfig) {
        var channel = pgEventConfig.channels()
                .stream()
                .filter(c -> c.equals("customer_evict"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No valid channel found for customer eviction"));
        this.endpoint = "pgevent://postgres/%s?datasource=#pgevent".formatted(channel);
    }

    @Override
    public void configure() throws Exception {
        from(endpoint)
                .process(exchange -> {
                    MDC.put("customerId", exchange.getIn().getBody(String.class));
                })
                .bean(CustomerCacheRepository.class, "evict")
                .bean(CustomerRedisRepository.class, "evict")
                .process(exchange -> MDC.clear());
    }
}
