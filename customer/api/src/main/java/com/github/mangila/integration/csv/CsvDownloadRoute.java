package com.github.mangila.integration.csv;

import com.github.mangila.config.AppConfig;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;

import java.util.Map;

@ApplicationScoped
public class CsvDownloadRoute extends RouteBuilder {

    public static final String ROUTE_ID = "csv-download-route";

    private final AppConfig.IoConfig ioConfig;

    public CsvDownloadRoute(AppConfig.IoConfig ioConfig) {
        this.ioConfig = ioConfig;
    }

    @Override
    public void configure() throws Exception {
        from("direct:%s".formatted(ROUTE_ID))
                .routeId(ROUTE_ID)
                .log("Starting processing: ${header.fileName} - ${header.domain}")
                .to("sql:select * from customer order by name?outputType=StreamList")
                .split(body())
                .streaming()
                .log("Processing SQL row: ${body}")
                .process(exchange -> {
                    var row = exchange.getMessage()
                            .getBody(Map.class);
                    var csv = new CustomerCsvRecord(
                            String.valueOf(row.get("ID")),
                            String.valueOf(row.get("NAME")),
                            String.valueOf(row.get("ADDRESS")),
                            String.valueOf(row.get("EMAIL")),
                            String.valueOf(row.get("PHONE"))
                    );
                    exchange.getMessage().setBody(csv);
                })
                .log("Writing CSV row: ${body}")
                .marshal()
                .bindy(BindyType.Csv, CustomerCsvRecord.class)
                .to("file:%s?fileName=${header.fileName}&fileExist=Append".formatted(ioConfig.downloadDirectory()));
    }
}
