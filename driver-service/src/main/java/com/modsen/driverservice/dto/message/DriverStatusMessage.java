package com.modsen.driverservice.dto.message;

import com.modsen.driverservice.entity.DriverStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DriverStatusMessage(
        UUID driverId,
        DriverStatus status
) {
}
