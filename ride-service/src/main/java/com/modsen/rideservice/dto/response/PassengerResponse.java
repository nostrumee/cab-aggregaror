package com.modsen.rideservice.dto.response;

import lombok.Builder;

@Builder
public record PassengerResponse(
        Long id,
        String firstName,
        String email
) {
}
