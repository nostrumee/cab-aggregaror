package com.modsen.passengerservice.dto.response;

public record PassengerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
