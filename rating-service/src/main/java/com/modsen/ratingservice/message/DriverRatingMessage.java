package com.modsen.ratingservice.message;

public record DriverRatingMessage(
        Long rideId,
        Long driverId,
        Integer rating
) {
}
