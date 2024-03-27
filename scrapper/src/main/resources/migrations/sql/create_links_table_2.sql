--liquibase formatted sql

--changeset anekoss:2
-- comment: create link table
create table if not exists links
(
    id         bigint generated always as identity primary key,
    uri   text                     not null unique,
    link_type text not null,
    updated_at timestamp with time zone not null,
    checked_at timestamp with time zone not null
);
-- rollback DROP TABLE link;
