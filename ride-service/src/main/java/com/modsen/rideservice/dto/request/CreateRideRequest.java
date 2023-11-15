package com.modsen.rideservice.dto.request;

public record CreateRideRequest(
        Long passengerId,
        String startPoint,
        String destinationPoint
) {
}
