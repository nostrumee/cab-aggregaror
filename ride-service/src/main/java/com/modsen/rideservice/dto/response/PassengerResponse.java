package com.modsen.rideservice.dto.response;

import lombok.Builder;

@Builder
public record PassengerResponse(
        String firstName,
        String email
) {
}
