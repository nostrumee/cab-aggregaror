package com.modsen.rideservice.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PassengerResponse(
        UUID id,
        String firstName,
        String email
) {
}
