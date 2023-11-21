package com.modsen.rideservice.dto.message;

import lombok.Builder;

@Builder
public record PassengerRatingMessage(
        Long rideId,
        Long passengerId,
        Integer rating
) {
}
