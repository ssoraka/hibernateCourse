drop TABLE users;
drop TABLE company;
drop TABLE profile;

create table company (
    id serial primary key,
    name varchar(64) not null unique
);

create table profile (
    id serial primary key,
    user_id bigint not null unique references users(id),
    street varchar(128),
    language char(2)
)

create table users (
    id serial primary key,
    username varchar(128) unique ,
    firstname varchar(128),
    lastname varchar(128),
    marriage_date date,
    birth_date date,
    role varchar(32),
    info JSONB,
    company_id int references company(id)
);

create table users_chat (
    user_id bigint references users(id),
    chat_id bigint references chat(id),
    primary key (user_id, chat_id)
)

create table chat (
    id bigserial primary key,
    name varchar(64) not null unique
);


--create sequence users_id_seq owned by users.id;