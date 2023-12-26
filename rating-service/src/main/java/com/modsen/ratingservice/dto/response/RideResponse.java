package com.modsen.ratingservice.dto.response;

import com.modsen.ratingservice.entity.RideStatus;
import lombok.Builder;

@Builder
public record RideResponse(
        Long passengerId,
        Long driverId,
        RideStatus status
) {
}
