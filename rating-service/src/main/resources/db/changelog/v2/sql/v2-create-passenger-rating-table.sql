drop table if exists passenger_rating;

create table passenger_rating
(
    id           bigserial primary key,
    ride_id      bigint not null,
    passenger_id uuid   not null,
    rating       int    not null
);