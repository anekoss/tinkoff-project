--liquibase formatted sql

--changeset anekoss:3
-- comment: create chat_links table
create table if not exists tg_chat_links
(
    id      bigint generated always as identity primary key,
    tg_chat_id bigint not null references tg_chats (id) on delete cascade,
    link_id bigint not null references links (id) on delete cascade
    );
-- rollback DROP TABLE chat_links;
