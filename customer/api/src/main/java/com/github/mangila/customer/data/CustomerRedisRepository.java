package com.github.mangila.customer.data;

import com.github.mangila.customer.web.CustomerDto;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import org.jspecify.annotations.Nullable;

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

    /**
     * Call a Redis function to let replicas race for a Lock and then run a DEL
     * OR just call DEL directly from all replicas,
     * OR Postgres connects to Redis directly and performs a DEL on redis and sends LISTEN/NOTIFY to the L1 cache(s)
     */
    public void evict(String key) {
        Log.info("Evict L2");
    }

    @Nullable
    public CustomerDto getIfPresent(UUID id) {
        return valueCommands.get(id.toString());
    }
}
