package com.github.mangila.config;

import io.smallrye.config.ConfigMapping;
import jakarta.validation.constraints.Size;

import java.nio.file.Path;
import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    @ConfigMapping(prefix = "app.io")
    interface IoConfig {
        Path uploadDirectory();

        Path downloadDirectory();
    }

    @ConfigMapping(prefix = "app.integration")
    interface IntegrationConfig {

        @ConfigMapping(prefix = "app.integration.pgevent")
        interface PgEvent {
            @Size(max = 3)
            List<String> channels();
        }
    }
}
