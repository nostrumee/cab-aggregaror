package com.modsen.rideservice.dto.message;

import lombok.Builder;

@Builder
public record CreateRideMessage(
        Long rideId
) {
}
