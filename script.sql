drop TABLE users;
drop TABLE company;
drop TABLE profile;
drop TABLE users_chat;

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
    id serial primary key,
    user_id bigint references users(id),
    chat_id bigint references chat(id),
    created_at timestamp not null,
    created_by varchar(128) not null,
    unique (user_id, chat_id)
)

create table chat (
    id bigserial primary key,
    name varchar(64) not null unique
);

create table company_locale (
    company_id int not null references company(id),
    lang char(2) not null,
    description varchar(128) not null,
    primary key (company_id, lang)
);


--create sequence users_id_seq owned by users.id;