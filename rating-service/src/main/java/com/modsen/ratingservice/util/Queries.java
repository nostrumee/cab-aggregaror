package com.modsen.ratingservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Queries {
    public final String FIND_PASSENGER_RATING_QUERY = """
            select avg(rating)
            from
            (
                select r.rating as rating
                from PassengerRating r
                where r.passengerId = :passengerId
                order by r.id desc
                limit 10
            )
            """;
    public final String FIND_DRIVER_RATING_QUERY = """
            select avg(rating)
            from
            (
                select r.rating as rating
                from DriverRating r
                where r.driverId = :driverId
                order by r.id desc
                limit 10
            )
            """;
}
