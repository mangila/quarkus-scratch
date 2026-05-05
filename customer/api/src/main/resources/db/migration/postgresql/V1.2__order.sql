CREATE TABLE order_entry
(
    id UUID PRIMARY KEY NOT NULL,
    customer_id UUID NOT NULL,
    products JSONB NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);
