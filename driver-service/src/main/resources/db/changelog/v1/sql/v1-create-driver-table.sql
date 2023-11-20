create table driver
(
    id             bigserial primary key,
    first_name     varchar(25)         not null,
    last_name      varchar(25)         not null,
    licence_number varchar(10) unique  not null,
    email          varchar(255) unique not null,
    phone          varchar(10) unique  not null,
    status         varchar(15)         not null default 'AVAILABLE'
);