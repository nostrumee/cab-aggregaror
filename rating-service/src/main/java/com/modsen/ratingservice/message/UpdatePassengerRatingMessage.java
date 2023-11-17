package com.modsen.ratingservice.message;

import lombok.Builder;

@Builder
public record UpdatePassengerRatingMessage(
        Long passengerId,
        Double rating
) {
}
