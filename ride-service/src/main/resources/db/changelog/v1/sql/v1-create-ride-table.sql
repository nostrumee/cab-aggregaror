create table ride
(
    id                bigserial primary key,
    passenger_id      bigint                      not null,
    driver_id         bigint,
    start_point       varchar(255)                not null,
    destination_point varchar(255)                not null,
    status            varchar(25)                 not null,
    created_date      timestamp without time zone not null,
    accepted_date     timestamp without time zone,
    start_date        timestamp without time zone,
    finish_date       timestamp without time zone,
    estimated_cost    numeric(3, 1)
);