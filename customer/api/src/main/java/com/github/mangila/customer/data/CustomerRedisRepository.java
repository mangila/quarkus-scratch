package com.github.mangila.customer.data;

import com.github.mangila.customer.web.dto.CustomerDto;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;

/**
 * L2 Cache
 */
@ApplicationScoped
public class CustomerRedisRepository {

    private static final String KEY_PREFIX = "customer::";

    private final ValueCommands<String, CustomerDto> valueCommands;

    public CustomerRedisRepository(RedisDataSource redisDataSource) {
        this.valueCommands = redisDataSource.value(String.class, CustomerDto.class);
    }

    @Nullable
    public CustomerDto getIfPresent(UUID id) {
        final var key = getkey(id);
        final var value = valueCommands.get(key);
        if (value != null) {
            Log.info("L2 Hit");
            return value;
        } else {
            Log.info("L2 Miss");
            return null;
        }
    }

    public void put(UUID id, CustomerDto dto) {
        Log.info("L2 Put");
        final var key = getkey(id);
        valueCommands.set(key, dto, new SetArgs().ex(Duration.ofHours(1)));
    }

    /**
     * Call a Redis function to let replicas race for a Lock and then run a DEL
     * OR just call DEL directly from all replicas,
     * OR Postgres connects to Redis directly and performs a DEL on redis and sends LISTEN/NOTIFY to the L1 cache(s)
     */
    public void evict(String key) {
        Log.info("L2 Evict");
        valueCommands.getdel(getkey(key));
    }

    private static String getkey(@NotNull UUID id) {
        return KEY_PREFIX + id;
    }

    private static String getkey(@NotBlank String id) {
        return KEY_PREFIX + id;
    }
}
