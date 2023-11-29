package com.modsen.ratingservice.dto.message;

import lombok.Builder;

@Builder
public record UpdatePassengerRatingMessage(
        Long passengerId,
        Double rating
) {
}
