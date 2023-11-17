package com.modsen.ratingservice.message;

import lombok.Builder;

@Builder
public record UpdateDriverRatingMessage(
        Long driverId,
        Double rating
) {
}
