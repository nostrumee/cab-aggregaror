package com.modsen.driverservice.dto.message;

import lombok.Builder;

@Builder
public record AcceptRideMessage(
        Long rideId,
        Long driverId
) {
}
