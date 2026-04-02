CREATE UNLOGGED TABLE ttl_cache
(
    key        TEXT PRIMARY KEY,
    payload    JSONB     NOT NULL,
    expires_at TIMESTAMP NOT NULL
);