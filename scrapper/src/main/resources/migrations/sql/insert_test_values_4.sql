--liquibase formatted sql

--changeset users:4
-- comment: insert tg_chats table values
insert into tg_chats(id, chat_id) values (1, 124025);
insert into tg_chats(id, chat_id) values (2, 327034);
insert into tg_chats(id, chat_id) values (3, 444444);
insert into tg_chats(id, chat_id) values (4, 444444);



--changeset users:5
-- comment: insert links table values
insert into links(id, uri, link_type, updated_at, checked_at) values (1, 'https://github.com/anekoss/tinkoff','GITHUB' '2024-01-01 10:00:00', '2024-01-01 13:00:00');
insert into links(id, uri, link_type, updated_at, checked_at) values (2,'https://stackoverflow.com/questions/59339862/retrieving-text-body-of-answers-and-comments-using-stackexchange-api', STACKOVERFLOW, '2024-02-01 13:00:54', '2024-02-05 13:00:00');
insert into links(id, uri, link_type, updated_at, checked_at) values (3, 'https://stackoverflow.com/questions/44760112/marching-cubes-generating-holes-in-mesh',  STACKOVERFLOW, '2024-03-27 13:00:54', '2024-03-26 13:00:00');


--changeset users:5
-- comment: insert tg_chat_links table values
insert into tg_chat_links(id, tg_chat_id, link_id) values (1, 1, 1);
insert into tg_chat_links(id, tg_chat_id, link_id) values (2, 1, 2);
insert into tg_chat_links(id, tg_chat_id, link_id) values (3, 1, 3);
insert into tg_chat_links(id, tg_chat_id, link_id) values (4, 2, 2);
insert into tg_chat_links(id, tg_chat_id, link_id) values (5, 2, 3);
insert into tg_chat_links(id, tg_chat_id, link_id) values (5, 3, 1);






