create table driver_rating
(
    id bigserial primary key,
    ride_id bigint not null,
    driver_id bigint not null,
    rating int not null
);