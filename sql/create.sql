DROP TABLE IF EXISTS user;

DROP DATABASE IF EXISTS login_app;
CREATE DATABASE login_app;

USE login_app;

CREATE TABLE user(
    user_name VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY(user_name)
);

INSERT INTO user (user_name, password) VALUES
(
    'user',
    'password'
),
(
    'pompom',
    'purin'
);

CREATE TABLE line(
    user_name VARCHAR(255),
    line_id VARCHAR(255),
    nonce VARCHAR(255)
    PRIMARY KEY(user_name)
);
