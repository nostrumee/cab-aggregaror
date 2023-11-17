create table passenger_rating
(
    id bigserial primary key,
    ride_id bigint not null,
    passenger_id bigint not null,
    rating int not null
);