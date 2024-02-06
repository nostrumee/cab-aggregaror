drop table if exists driver_rating;

create table driver_rating
(
    id        bigserial primary key,
    ride_id   bigint not null,
    driver_id uuid   not null,
    rating    int    not null
);