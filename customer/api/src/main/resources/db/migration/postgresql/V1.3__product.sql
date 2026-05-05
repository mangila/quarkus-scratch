CREATE TABLE product
(
    id UUID PRIMARY KEY NOT NULL,
    name TEXT NOT NULL UNIQUE,
    image_url TEXT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);
