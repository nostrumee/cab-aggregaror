package com.modsen.rideservice.dto.message;

import lombok.Builder;

@Builder
public record RideOrderMessage(
        Long rideId
) {
}
