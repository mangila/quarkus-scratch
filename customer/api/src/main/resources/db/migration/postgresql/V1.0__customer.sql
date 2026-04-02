create table customer
(
    id      UUID PRIMARY KEY NOT NULL,
    name    TEXT             NOT NULL,
    address TEXT             NOT NULL,
    email   TEXT             NOT NULL UNIQUE,
    phone   TEXT             NOT NULL,
    orders  JSONB            NOT NULL
);

create table customer_order
(
    id          UUID PRIMARY KEY NOT NULL,
    customer_id UUID             NOT NULL,
    products    JSONB            NOT NULL,
    price       NUMERIC(10, 2)   NOT NULL
);

create table product
(
    id        UUID PRIMARY KEY NOT NULL,
    name      TEXT             NOT NULL UNIQUE,
    image_url TEXT             NOT NULL,
    price     NUMERIC(10, 2)   NOT NULL
);