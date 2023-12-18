package com.modsen.driverservice.dto.message;

import com.modsen.driverservice.entity.DriverStatus;
import lombok.Builder;

@Builder
public record DriverStatusMessage(
        Long driverId,
        DriverStatus status
) {
}
