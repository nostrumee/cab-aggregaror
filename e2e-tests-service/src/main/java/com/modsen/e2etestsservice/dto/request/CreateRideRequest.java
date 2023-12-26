package com.modsen.e2etestsservice.dto.request;

import lombok.Builder;

@Builder
public record CreateRideRequest(
        Long passengerId,
        String startPoint,
        String destinationPoint
) {
}
