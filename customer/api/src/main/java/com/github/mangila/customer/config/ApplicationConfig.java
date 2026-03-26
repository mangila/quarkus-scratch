package com.github.mangila.customer.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface ApplicationConfig {
    String secret();

    @ConfigMapping(prefix = "app.integration")
    interface IntegrationConfig {
        String secret();
    }
}
