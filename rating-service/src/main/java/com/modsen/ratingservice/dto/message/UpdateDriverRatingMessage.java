package com.modsen.ratingservice.dto.message;

import lombok.Builder;

@Builder
public record UpdateDriverRatingMessage(
        Long driverId,
        Double rating
) {
}
