package com.modsen.endtoendtests.dto.request;

import lombok.Builder;

@Builder
public record CreateRideRequest(
        Long passengerId,
        String startPoint,
        String destinationPoint
) {
}
