package com.modsen.rideservice.dto.message;

import lombok.Builder;

@Builder
public record DriverRatingMessage(
        Long rideId,
        Long driverId,
        Integer rating
) {
}
