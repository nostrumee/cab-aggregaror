package com.modsen.rideservice.dto.request;

public record CreateRideOrderRequest(
        Long passengerId,
        String startPoint,
        String destinationPoint
) {
}
