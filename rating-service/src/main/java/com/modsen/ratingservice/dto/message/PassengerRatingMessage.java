package com.modsen.ratingservice.dto.message;

import lombok.Builder;

@Builder
public record PassengerRatingMessage(
        Long passengerId,
        Double rating
) {
}
