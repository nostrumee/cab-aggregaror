package com.modsen.endtoendtests.dto.request;

import lombok.Builder;

@Builder
public record DriverRatingRequest(
        Long rideId,
        Integer rating
) {
}
