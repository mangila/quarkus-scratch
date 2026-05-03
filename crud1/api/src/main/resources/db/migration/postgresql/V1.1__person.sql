create table person
(
    birth_date DATE   not null,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    version    bigint not null,
    id         uuid   not null,
    email      TEXT   not null unique,
    name       TEXT   not null,
    phone      TEXT   not null,
    properties JSONB  not null,
    primary key (id)
);

create table person_audit
(
    REV        integer not null,
    REVTYPE    smallint,
    birth_date DATE,
    updated_at timestamp(6) with time zone,
    id         uuid    not null,
    email      TEXT,
    name       TEXT,
    phone      TEXT,
    properties JSONB,
    primary key (REV, id)
);

alter table if exists person_audit
    add constraint FKbhx7sqiw4trc9x7kuc0j5neq5
        foreign key (REV)
            references REVINFO;