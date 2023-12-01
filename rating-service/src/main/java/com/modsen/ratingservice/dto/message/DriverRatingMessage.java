package com.modsen.ratingservice.dto.message;

import lombok.Builder;

@Builder
public record DriverRatingMessage(
        Long driverId,
        Double rating
) {
}
