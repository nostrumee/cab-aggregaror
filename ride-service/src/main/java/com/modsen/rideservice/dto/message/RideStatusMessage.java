package com.modsen.rideservice.dto.message;

import com.modsen.rideservice.entity.RideStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
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
