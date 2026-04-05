package com.github.mangila.integration.pgcache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TtlCacheRepository implements PanacheRepositoryBase<TtlCacheEntity, String> {

    @Transactional
    public TtlCacheEntity findNotExpired(String key) {
        return find("cacheKey = ?1 and expiresAt > current_timestamp", key)
                .firstResult();
    }

}
