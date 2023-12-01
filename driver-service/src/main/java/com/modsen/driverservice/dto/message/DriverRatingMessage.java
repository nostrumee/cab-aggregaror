package com.modsen.driverservice.dto.message;

public record DriverRatingMessage(
        Long driverId,
        Double rating
) {
}
