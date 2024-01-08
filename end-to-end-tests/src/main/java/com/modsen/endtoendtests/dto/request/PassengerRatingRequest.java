package com.modsen.endtoendtests.dto.request;

import lombok.Builder;

@Builder
public record PassengerRatingRequest(
        Long rideId,
        Integer rating
) {
}
