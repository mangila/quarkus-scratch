package com.github.mangila.integration.pgevent;

import com.github.mangila.config.AppConfig;
import com.github.mangila.customer.data.CustomerCacheRepository;
import com.github.mangila.shared.JsonService;
import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.jboss.logging.MDC;

/**
 * Listen to updates on the customer table and evict the corresponding cache entry using PostgreSQL LISTEN/NOTIFY.
 * <p>
 * If a notification is lost, we will have "dirty" data in the cache(s) for a short time until the TTL kicks in.
 */
@ApplicationScoped
public class CacheEvictRoute extends RouteBuilder {

    private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

    private final JsonService jsonService;
    private final CustomerCacheRepository customerCacheRepository;
    private final String endpoint;

    public CacheEvictRoute(AppConfig.IntegrationConfig.PgEvent pgEventConfig,
                           JsonService jsonService,
                           CustomerCacheRepository customerCacheRepository) {
        this.jsonService = jsonService;
        this.customerCacheRepository = customerCacheRepository;
        var channel = pgEventConfig.channels()
                .stream()
                .filter(c -> c.equals("cache_evict"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing channel 'cache_evict' in properties"));
        this.endpoint = PgEventUtils.getEndpoint(channel);
    }

    @Override
    public void configure() throws Exception {
        onCompletion()
                .onCompleteOnly()
                .log("Evicted cache entry")
                .process(_ -> MDC.clear());
        onException(Exception.class)
                .handled(true)
                .logExhausted(false)
                .log(LoggingLevel.ERROR, "Error evicting cache entry for payload ${body}: ${exception.message}");
        from(endpoint)
                .routeId("cache-evict")
                .unmarshal().json(JsonLibrary.Jackson, CacheEvictPayload.class)
                .to("bean-validator://validate-cache-evict-payload")
                .process(exchange -> {
                    var payload = exchange.getMessage(CacheEvictPayload.class);
                    MDC.put("id", payload.id());
                    exchange.getMessage()
                            .setHeader("tgTableName", payload.tgTableName());
                    exchange.getMessage()
                            .setBody(payload.id());
                })
                .choice()
                .when(simple("${header.tgTableName} == 'customer'"))
                .bean(customerCacheRepository, "evict")
                .when(simple("${header.tgTableName} == 'product'"))
                .bean(customerCacheRepository, "evict")
                .otherwise()
                .log("Unknown event: ${body} : ${header.tgTableName}");
    }

    public record CacheEvictPayload(@NotBlank String tgTableName, @NotBlank String id) {
    }
}
