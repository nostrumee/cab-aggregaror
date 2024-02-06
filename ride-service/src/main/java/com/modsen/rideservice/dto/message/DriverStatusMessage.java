package com.modsen.rideservice.dto.message;

import com.modsen.rideservice.entity.DriverStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DriverStatusMessage(
        UUID driverId,
        DriverStatus status
) {
}
