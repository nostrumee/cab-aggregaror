package com.modsen.rideservice.dto.response;

public record PassengerResponse(
        Long id,
        String firstName,
        String email
) {
}
