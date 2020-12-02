CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT,
    password TEXT,
    email TEXT,
    first_name TEXT,
    last_name TEXT,
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
    id SERIAL PRIMARY KEY
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
