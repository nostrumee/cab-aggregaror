package com.modsen.notificationservice.dto.message;

import com.modsen.notificationservice.dto.RideStatus;

import java.math.BigDecimal;

public record RideStatusMessage(
        Long rideId,
        RideStatus status,
        String passengerEmail,
        String passengerFirstName,
        String startPoint,
        String destinationPoint,
        BigDecimal estimatedCost
) {
}
