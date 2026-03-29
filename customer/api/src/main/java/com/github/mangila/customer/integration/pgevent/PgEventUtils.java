package com.github.mangila.customer.integration.pgevent;

import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;
import org.jspecify.annotations.NonNull;

public final class PgEventUtils {

    private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();

    private PgEventUtils() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static String getEndpoint(String channel) {
        ENSURE_STRING_OPS.notBlank(channel, "channel cannot be blank");
        return "pgevent://postgres/%s?datasource=#pgevent".formatted(channel);
    }
}
