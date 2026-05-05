CREATE TABLE customer
(
    id UUID PRIMARY KEY NOT NULL,
    name TEXT NOT NULL,
    address JSONB NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone TEXT NOT NULL,
    orders JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);

CREATE TABLE customer_audit
(
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    id UUID NOT NULL,
    email TEXT,
    name TEXT,
    phone TEXT,
    address JSONB,
    PRIMARY KEY (rev, id)
);

ALTER TABLE IF EXISTS customer_audit
ADD CONSTRAINT fk2tmkstrlk54gpmyy7ajwhq32j
FOREIGN KEY (rev)
REFERENCES revinfo;
