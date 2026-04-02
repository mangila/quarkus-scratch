package com.github.mangila.integration.pgevent;

import com.github.mangila.config.AppConfig;
import io.github.mangila.ensure4j.Ensure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.camel.ProducerTemplate;

@ApplicationScoped
public class PgEventProducer {

    private final AppConfig.IntegrationConfig.PgEvent pgEventConfig;
    private final ProducerTemplate producerTemplate;

    public PgEventProducer(AppConfig.IntegrationConfig.PgEvent pgEventConfig,
                           ProducerTemplate producerTemplate) {
        this.pgEventConfig = pgEventConfig;
        this.producerTemplate = producerTemplate;
    }

    public void sendBody(@NotBlank String channel, @NotNull Object body) {
        Ensure.isTrue(pgEventConfig.channels().contains(channel), "channel: %s - not configured".formatted(channel));
        final String endpoint = PgEventUtils.getEndpoint(channel);
        producerTemplate.sendBody(endpoint, body);
    }
}
