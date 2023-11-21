package com.modsen.rideservice.dto.message;

public record AcceptRideMessage(
        Long rideId,
        Long driverId
) {
}
