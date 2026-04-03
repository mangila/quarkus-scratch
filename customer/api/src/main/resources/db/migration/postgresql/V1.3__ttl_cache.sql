CREATE UNLOGGED TABLE ttl_cache
(
    cache_key        TEXT PRIMARY KEY,
    payload    JSONB     NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_ttl_cache ON ttl_cache USING GIN (payload);
CREATE INDEX idx_ttl_cache_expires_at ON ttl_cache (expires_at);