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

create table customer_audit
(
    REV        integer not null,
    REVTYPE    smallint,
    updated_at timestamp(6) with time zone,
    id         uuid    not null,
    email      TEXT,
    name       TEXT,
    phone      TEXT,
    address    JSONB,
    primary key (REV, id)
);

alter table if exists customer_audit
    add constraint FK2tmkstrlk54gpmyy7ajwhq32j
        foreign key (REV)
            references REVINFO;