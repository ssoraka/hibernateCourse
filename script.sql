drop TABLE users;

create table users
(
    username varchar(128) primary key,
    firstname varchar(128),
    lastname varchar(128),
    marriage_date date,
    birth_date date,
    role varchar(32),
    info JSONB
);