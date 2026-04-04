create table customer
(
    id         UUID PRIMARY KEY NOT NULL,
    name       TEXT             NOT NULL,
    address    JSONB            NOT NULL,
    email      TEXT             NOT NULL UNIQUE,
    phone      TEXT             NOT NULL,
    orders     JSONB            NOT NULL,
    created_at TIMESTAMPTZ      NOT NULL,
    updated_at TIMESTAMPTZ      NOT NULL,
    version    BIGINT           NOT NULL
);