package com.github.mangila.integration.pgcache;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TtlCacheRepository implements PanacheRepositoryBase<TtlCacheEntity, String> {
}
