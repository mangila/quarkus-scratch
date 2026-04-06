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
                .log("Starting processing: ${header.domain}")
                .to("sql:select * from customer order by name?outputType=StreamList")
                .split(body())
                .streaming()
                .process(exchange -> {
                    var map = exchange.getMessage().getBody(Map.class);
                    var csv = new CustomerCsvRecord();
                    csv.setId(String.valueOf(map.get("ID")));
                    csv.setName(String.valueOf(map.get("NAME")));
                    csv.setAddress(String.valueOf(map.get("ADDRESS")));
                    csv.setEmail(String.valueOf(map.get("EMAIL")));
                    csv.setPhone(String.valueOf(map.get("PHONE")));
                    exchange.getMessage().setBody(csv);
                })
                .log("Completed processing: ${body} records")
                .marshal()
                .bindy(BindyType.Csv, CustomerCsvRecord.class)
                .log("Writing customer csv row ${body}")
                .to("file:%s?fileName=%s.csv&fileExist=Append".formatted(ioConfig.downloadDirectory(), "customers"));
    }
}
