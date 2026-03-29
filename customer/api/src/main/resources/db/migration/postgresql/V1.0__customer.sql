create table customer
(
    id               UUID PRIMARY KEY NOT NULL,
    name             TEXT             NOT NULL,
    address          TEXT             NOT NULL UNIQUE,
    email            TEXT             NOT NULL UNIQUE,
    phone            TEXT             NOT NULL,
    favorite_pokemon JSONB            NOT NULL
);