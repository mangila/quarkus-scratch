package com.github.mangila.integration.pgcache;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity(name = "TtlCache")
@Table(name = "ttl_cache")
public class TtlCacheEntity {

    @Id
    @Column(name = "cache_key")
    private String cacheKey;
    @Type(JsonType.class)
    @Column(name = "payload", columnDefinition = "JSONB")
    private JsonNode payload;
    @Column(name = "expires_at")
    private Instant expiresAt;

    public static TtlCacheEntity of(String cacheKey, JsonNode value) {
        var expiresAt = Instant.now().plus(2, ChronoUnit.HOURS);
        return new TtlCacheEntity(cacheKey, value, expiresAt);
    }

    public TtlCacheEntity(String cacheKey, JsonNode value, Instant expiresAt) {
        this.cacheKey = cacheKey;
        this.payload = value;
        this.expiresAt = expiresAt;
    }

    public TtlCacheEntity() {
        // do nothing, for Hibernate
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode value) {
        this.payload = value;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Transient
    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }
}
