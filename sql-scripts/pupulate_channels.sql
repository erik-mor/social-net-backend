
insert into channels default values;
insert into channel_users(channel_id, user_id) values (1, 1);
insert into channel_users(channel_id, user_id) values (1, 2);

insert into channels default values;
insert into channel_users(channel_id, user_id) values (2, 3);
insert into channel_users(channel_id, user_id) values (2, 4);

insert into channels default values;
insert into channel_users(channel_id, user_id) values (3, 5);
insert into channel_users(channel_id, user_id) values (3, 6);

insert into posts (user_id, content) values (1, 'Post from jose');
insert into posts (user_id, content) values (2, 'Post from marko');
insert into posts (user_id, content) values (3, 'Post from ronaldo');
insert into posts (user_id, content) values (4, 'Post from erzo');
insert into posts (user_id, content) values (5, 'Post from zlatan');
insert into posts (user_id, content) values (6, 'Post from sergej');
insert into posts (user_id, content) values (7, 'Post from sergio');
insert into posts (user_id, content) values (8, 'Post from rob');
insert into posts (user_id, content) values (9, 'Post from jakub');
