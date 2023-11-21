package com.modsen.driverservice.dto.message;

public record UpdateDriverRatingMessage(
        Long driverId,
        Double rating
) {
}
