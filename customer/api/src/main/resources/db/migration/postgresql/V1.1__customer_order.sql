create table customer_order
(
    id          UUID PRIMARY KEY NOT NULL,
    customer_id UUID             NOT NULL,
    products    JSONB            NOT NULL,
    price       NUMERIC(10, 2)   NOT NULL
);