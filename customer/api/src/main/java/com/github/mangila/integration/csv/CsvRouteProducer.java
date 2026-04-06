package com.github.mangila.integration.csv;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import java.util.Map;

@ApplicationScoped
public class CsvRouteProducer {

    private final ProducerTemplate producerTemplate;

    public CsvRouteProducer(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public Exchange upload(Object body, Map<String, Object> headers) {
        return producerTemplate.send("direct:%s".formatted(CsvUploadRoute.ROUTE_ID), exchange -> {
            var message = exchange.getMessage();
            message.setBody(body);
            message.setHeaders(headers);
        });
    }

    public Exchange download(Object body, Map<String, Object> headers) {
        return producerTemplate.send("direct:%s".formatted(CsvDownloadRoute.ROUTE_ID), exchange -> {
            var message = exchange.getMessage();
            message.setBody(body);
            message.setHeaders(headers);
        });
    }

}
