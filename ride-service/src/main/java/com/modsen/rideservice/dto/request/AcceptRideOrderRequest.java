package com.modsen.rideservice.dto.request;

public record AcceptRideOrderRequest(
        Long rideId,
        Long driverId
) {
}
