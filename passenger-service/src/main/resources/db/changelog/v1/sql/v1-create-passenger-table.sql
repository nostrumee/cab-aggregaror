create table passenger
(
    id         bigserial,
    first_name varchar(25)         not null,
    last_name  varchar(25)         not null,
    email      varchar(255) unique not null,
    phone      varchar(10) unique  not null
);