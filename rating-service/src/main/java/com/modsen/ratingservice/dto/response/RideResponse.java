package com.modsen.ratingservice.dto.response;

import com.modsen.ratingservice.entity.RideStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RideResponse(
        UUID passengerId,
        UUID driverId,
        RideStatus status
) {
}
