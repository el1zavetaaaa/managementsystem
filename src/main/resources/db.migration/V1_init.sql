DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

create table employees
(
    id         bigserial primary key,
    first_name text        not null,
    last_name  text        not null,
    email      text unique not null
);

create table managers
(
    id   bigserial primary key,
    name text not null
);
