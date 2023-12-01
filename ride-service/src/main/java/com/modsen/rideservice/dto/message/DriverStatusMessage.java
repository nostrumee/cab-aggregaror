package com.modsen.rideservice.dto.message;

import com.modsen.rideservice.entity.DriverStatus;
import lombok.Builder;

@Builder
public record DriverStatusMessage(
        Long driverId,
        DriverStatus status
) {
}
