package com.modsen.e2etestsservice.dto.request;

import lombok.Builder;

@Builder
public record PassengerRatingRequest(
        Long rideId,
        Integer rating
) {
}
