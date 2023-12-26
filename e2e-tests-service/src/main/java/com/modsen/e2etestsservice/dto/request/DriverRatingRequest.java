package com.modsen.e2etestsservice.dto.request;

import lombok.Builder;

@Builder
public record DriverRatingRequest(
        Long rideId,
        Integer rating
) {
}
