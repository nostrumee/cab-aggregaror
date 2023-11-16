package com.modsen.ratingservice.dto.message;

public record PassengerRatingMessage(
        Long rideId,
        Long passengerId,
        Integer rating
) {
}
