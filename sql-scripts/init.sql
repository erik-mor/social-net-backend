CREATE TYPE player_position AS ENUM ('GK', 'ST', 'CM', 'CB', 'LW', 'RW', 'LB', 'RB');

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT,
    password TEXT,
    email TEXT,
    first_name TEXT,
    last_name TEXT,
    club TEXT,
    position player_position null,
    latitude double precision null,
    longitude double precision null,
    is_manager boolean DEFAULT false,
    is_archived boolean DEFAULT false
);

CREATE TABLE follower_following (
    id SERIAL PRIMARY KEY,
    follower_id INT,
    following_id INT,
    FOREIGN KEY (follower_id) REFERENCES users(id),
    FOREIGN KEY (following_id) REFERENCES users(id)
);

CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    user_id int,
    content TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE channels (
    id SERIAL PRIMARY KEY,
    is_archived boolean default false
);

CREATE TABLE messages (
    id SERIAL PRIMARY KEY,
    message text,
    channel_id Int,
    sender_id Int,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (channel_id) REFERENCES channels(id)
);

CREATE TABLE channel_users (
    id SERIAL PRIMARY KEY,
    channel_id int,
    user_id Int,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (channel_id) REFERENCES channels(id)
);

insert into
    users(username, email, password, first_name, last_name, club, is_manager, longitude, latitude)
values ('jose', 'josem@gmail.com', '$2a$12$Q27YMlft91Xkr56a7M9KTe2nDI3n2pYqkf38vO9x7ScE5mjTNR6Z2', 'Jose', 'Mourinho', 'Totenham H', true, 9.1393, 38.707008);

insert into
    users(username, email, password, first_name, last_name, club, is_manager, longitude, latitude)
values ('marko', 'petrMarko@gmail.com', '$2a$12$cdkqPtkWuQdV5Zfrm5pb/O6avPwHM1mzb4FN5ynks6ELJVR57R1q6', 'Peter', 'Marko', '1.FBC Zvolen', true, 19.1462, 48.7363);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('ronaldo', 'cronaldo@gmail.com', '$2a$12$gk2qoEKac2r8lS9rrlxHn.8vV3cXBDZo23HCUxJ.A4XTDbTD77T3G', 'Cristiano', 'Ronaldo', 'Juventus Turin', 'ST', false, 7.6869, 45.0703);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('erzo', 'erikMor@gmail.com', '$2a$12$rY.x4K/.ki4MYdAnU36Z.ONYQ92./IufhcSRSVewc3wl4Z1i3QtLO', 'Erik', 'Moravcik', '1.FBC Zvolen', 'ST', false, 19.1371, 48.5762);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('zlatan', 'zlatanIbra@gmail.com', '$2a$12$bEHEaotQ6HCQIZQ0Rgo15u.9k65rD/joXCxTbFhXzmguE6qJMcITG', 'Zlatan', 'Ibrahimovic', 'AC Milan', 'ST', false, 18.0686, 59.3293);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('sergej', 'sergKnab@gmail.com', '$2a$12$gN5pELUKUEz5NrC/hX/shuGQ.Lu7hlOdK2DskfwiBj7sJdA6WAteu', 'Sergej', 'Knabry', 'Lokomotiv Moscov', 'CM', false, 37.6173, 55.7558);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('sergio', 'sergioRamos@gmail.com', '$2a$12$El0BhQz3IpfKAyBBDDBeOuKViLYslkrIAR774K6fa/hTj/CCM9HNK', 'Sergio', 'Ramos', 'Real Madrid', 'CB', false, 3.7038, 40.4168);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('rob', 'robLewa@gmail.com', '$2a$12$A3x5eJRW5Hz3CuJqpew.wuChSEyHq5w5KBIlI0V1WbG1A4XWzomXq', 'Robert', 'Lewandowski', 'Bayern Munich', 'ST', false, 13.4050, 52.5200);

insert into
    users(username, email, password, first_name, last_name, club, position, is_manager, longitude, latitude)
values ('jakub', 'jakubBlzscyk@gmail.com', '$2a$12$HsqLFhOGnc4DxHHCOo55M.NWqKEgbBM0lblIJhCRaEKE1tPPLaZlm', 'Jakub', 'Blazscyk', 'Varszava', 'LB', false, 21.0122, 52.2297);


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
