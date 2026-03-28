create table customer
(
    id               UUID  NOT NULL,
    name             TEXT  NOT NULL,
    address          TEXT  NOT NULL,
    email            TEXT  NOT NULL,
    phone            TEXT  NOT NULL,
    favorite_pokemon JSONB NOT NULL,
    primary key (id)
);