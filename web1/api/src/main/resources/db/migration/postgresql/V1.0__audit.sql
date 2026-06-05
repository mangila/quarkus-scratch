CREATE SEQUENCE public.revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE public.revinfo
(
    rev INTEGER NOT NULL,
    revtstmp BIGINT,
    PRIMARY KEY (rev)
);
