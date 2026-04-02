package com.github.mangila.config;

import io.smallrye.config.ConfigMapping;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    @ConfigMapping(prefix = "app.integration")
    interface IntegrationConfig {

        @ConfigMapping(prefix = "app.integration.pgevent")
        interface PgEvent {
            @Size(max = 1)
            List<String> channels();
        }

        @ConfigMapping(prefix = "app.integration.pokeapi")
        interface PokeApi {
            @Valid
            Secret token();
        }
    }
}
