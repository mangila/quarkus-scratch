CREATE TABLE person
(
    id UUID NOT NULL,
    person_name TEXT NOT NULL,
    birth_date DATE NOT NULL,
    email TEXT NOT NULL UNIQUE,
    phone TEXT NOT NULL,
    properties JSONB NOT NULL,
    rev_version BIGINT NOT NULL,
    created_at TIMESTAMP(6) WITH TIME ZONE,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE TABLE person_audit
(
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    id UUID NOT NULL,
    person_name TEXT,
    birth_date DATE,
    email TEXT,
    phone TEXT,
    properties JSONB,
    updated_at TIMESTAMP(6) WITH TIME ZONE,
    PRIMARY KEY (rev, id)
);

ALTER TABLE IF EXISTS person_audit
ADD CONSTRAINT fkbhx7sqiw4trc9x7kuc0j5neq5
FOREIGN KEY (rev)
REFERENCES revinfo;
