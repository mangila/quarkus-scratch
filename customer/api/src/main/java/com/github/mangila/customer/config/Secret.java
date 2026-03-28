package com.github.mangila.customer.config;

import com.fasterxml.jackson.annotation.JsonValue;
import org.jspecify.annotations.NonNull;

public class Secret {

    private final String value;

    public Secret(String value) {
        this.value = value;
    }

    public String reveal() {
        return value;
    }

    @Override
    @NonNull
    public String toString() {
        return "<hidden>";
    }

    @JsonValue
    public String serialize() {
        return "<hidden>";
    }
}
