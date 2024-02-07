package com.modsen.driverservice.dto.message;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AcceptRideMessage(
        Long rideId,
        UUID driverId
) {
}
