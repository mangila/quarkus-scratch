package com.github.mangila.customer.data;

import com.github.mangila.customer.web.dto.CustomerDto;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;

/**
 * L2 Cache
 */
@ApplicationScoped
public class CustomerRedisRepository {

    private final ValueCommands<String, CustomerDto> valueCommands;

    public CustomerRedisRepository(RedisDataSource redisDataSource) {
        this.valueCommands = redisDataSource.value(String.class, CustomerDto.class);
    }

    @Nullable
    public CustomerDto getIfPresent(@NotNull UUID id) {
        final var value = valueCommands.get(id.toString());
        if (value != null) {
            Log.info("L2 Cache hit");
            return value;
        } else {
            Log.info("L2 Cache miss");
            return null;
        }
    }

    public void put(@NotNull UUID id, @Valid CustomerDto dto) {
        Log.info("Put L2");
        valueCommands.set(id.toString(), dto, new SetArgs().ex(Duration.ofHours(1)));
    }

    /**
     * Call a Redis function to let replicas race for a Lock and then run a DEL
     * OR just call DEL directly from all replicas,
     * OR Postgres connects to Redis directly and performs a DEL on redis and sends LISTEN/NOTIFY to the L1 cache(s)
     */
    public void evict(String key) {
        Log.info("Evict L2");
    }
}
