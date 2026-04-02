package com.github.mangila.config;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.NonNull;


/**
 * Represents a sensitive value intended to be hidden or obfuscated when exposed externally.
 * Provides mechanisms to securely handle and serialize the sensitive data without revealing its value.
 * <p>
 * Instances of this class are immutable.
 */
public final class Secret {

    private static final String OBFUSCATED = "<hidden>";

    private final String value;

    public Secret(@NotBlank String value) {
        this.value = value;
    }

    public String reveal() {
        return value;
    }

    @Override
    @NonNull
    public String toString() {
        return OBFUSCATED;
    }

    @JsonValue
    @NonNull
    public String serialize() {
        return OBFUSCATED;
    }
}
