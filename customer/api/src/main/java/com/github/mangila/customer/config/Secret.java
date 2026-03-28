package com.github.mangila.customer.config;

import com.fasterxml.jackson.annotation.JsonValue;
import org.jspecify.annotations.NonNull;


/**
 * Represents a sensitive value intended to be hidden or obfuscated when exposed externally.
 * Provides mechanisms to securely handle and serialize the sensitive data without revealing its value.
 * <p>
 * Instances of this class are immutable.
 */
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
