package com.modsen.driverservice.dto.message;

import lombok.Builder;

import java.util.UUID;

@Builder
public record DriverRatingMessage(
        UUID driverId,
        Double rating
) {
}
