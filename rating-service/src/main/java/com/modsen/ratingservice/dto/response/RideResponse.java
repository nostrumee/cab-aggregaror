package com.modsen.ratingservice.dto.response;

import com.modsen.ratingservice.entity.RideStatus;

public record RideResponse(
        Long passengerId,
        Long driverId,
        RideStatus status
) {
}
