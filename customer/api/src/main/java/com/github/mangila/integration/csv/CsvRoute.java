package com.github.mangila.integration.csv;

import com.github.mangila.integration.pgcache.TtlCacheRepository;
import com.github.mangila.shared.JsonService;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.MDC;

@ApplicationScoped
public class CsvRoute extends RouteBuilder {

    public static final String ROUTE_ID = "csv-route";

    private final TtlCacheRepository ttlCacheRepository;
    private final JsonService jsonService;
    private final String fileEndpoint;

    public CsvRoute(TtlCacheRepository ttlCacheRepository,
                    JsonService jsonService,
                    @ConfigProperty(name = "quarkus.http.body.uploads-directory") String uploadsDirectory) {
        this.ttlCacheRepository = ttlCacheRepository;
        this.jsonService = jsonService;
        this.fileEndpoint = new StringBuilder("file:")
                .append(uploadsDirectory)
                // Params
                .append("?")
                .append("fileName=${body}")
                .append("&")
                .append("delete=true")
                .toString();
    }

    @Override
    public void configure() throws Exception {
        onCompletion()
                .onCompleteOnly()
                .log("Completed processing ${body.size} records");
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .log("Reading: ${header.original} - ${body}")
                .pollEnrich()
                .simple(fileEndpoint)
                .timeout(5000)
                .choice()
                .when(simple("${header.domain} == 'customer'"))
                .to("direct:%s".formatted(CustomerCsvRoute.ROUTE_ID))
                .when(simple("${header.domain} == 'product'"))
                .to("direct:%s".formatted(ProductCsvRoute.ROUTE_ID))
                .otherwise()
                .log("Unknown domain: ${header.domain}");
    }
}
