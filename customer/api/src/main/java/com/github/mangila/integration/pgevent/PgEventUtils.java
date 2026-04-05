package com.github.mangila.integration.pgevent;

import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;
import org.jspecify.annotations.NonNull;

final class PgEventUtils {

    private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

    PgEventUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    static String getEndpoint(String channel) {
        ENSURE_STRING_OPS.notBlank(channel, "channel cannot be blank");
        return "pgevent:///quarkus/%s?datasource=#pgevent".formatted(channel);
    }
}
