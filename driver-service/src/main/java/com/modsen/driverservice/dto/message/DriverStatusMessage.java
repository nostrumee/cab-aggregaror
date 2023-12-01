package com.modsen.driverservice.dto.message;

import com.modsen.driverservice.entity.DriverStatus;

public record DriverStatusMessage(
        Long driverId,
        DriverStatus status
) {
}
