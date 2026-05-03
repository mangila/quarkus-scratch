create sequence REVINFO_SEQ start with 1 increment by 50;

create table REVINFO
(
    REV      integer not null,
    REVTSTMP bigint,
    primary key (REV)
);