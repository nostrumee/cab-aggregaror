package com.modsen.ratingservice.message;

public record PassengerRatingMessage(
        Long rideId,
        Long passengerId,
        Integer rating
) {
}
