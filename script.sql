drop TABLE users;

create table users
(
    id bigint primary key,
    username varchar(128) unique ,
    firstname varchar(128),
    lastname varchar(128),
    marriage_date date,
    birth_date date,
    role varchar(32),
    info JSONB
);

create sequence users_id_seq owned by users.id;