package com.modsen.ratingservice.dto.message;

public record DriverRatingMessage(
        Long rideId,
        Long driverId,
        Integer rating
) {
}
