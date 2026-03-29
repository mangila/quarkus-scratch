package com.github.mangila.customer.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    @ConfigMapping(prefix = "app.integration")
    interface IntegrationConfig {

        @ConfigMapping(prefix = "app.integration.pgevent")
        interface PgEvent {
            List<String> channels();
        }

        @ConfigMapping(prefix = "app.integration.pokeapi")
        interface PokeApi {
            Secret token();
        }
    }
}
