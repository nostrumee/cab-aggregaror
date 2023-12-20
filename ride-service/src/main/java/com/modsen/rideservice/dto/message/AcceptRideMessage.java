package com.modsen.rideservice.dto.message;

import lombok.Builder;

@Builder
public record AcceptRideMessage(
        Long rideId,
        Long driverId
) {
}
