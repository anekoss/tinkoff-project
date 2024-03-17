--liquibase formatted sql

--changeset anekoss:1
-- comment: create chat table
create table if not exists chats
(
    id      bigint generated always as identity primary key,
    chat_id bigint not null unique
);
-- rollback DROP TABLE chat;