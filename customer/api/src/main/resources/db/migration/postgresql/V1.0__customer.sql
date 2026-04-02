create table customer
(
    id      UUID PRIMARY KEY NOT NULL,
    name    TEXT             NOT NULL,
    address TEXT             NOT NULL,
    email   TEXT             NOT NULL UNIQUE,
    phone   TEXT             NOT NULL,
    orders  JSONB            NOT NULL
);