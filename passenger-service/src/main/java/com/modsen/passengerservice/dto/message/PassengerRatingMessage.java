package com.modsen.passengerservice.dto.message;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PassengerRatingMessage(
        UUID passengerId,
        Double rating
) {
}
