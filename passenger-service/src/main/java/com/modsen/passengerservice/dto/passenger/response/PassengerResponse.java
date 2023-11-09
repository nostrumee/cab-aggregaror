package com.modsen.passengerservice.dto.passenger.response;

public record PassengerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
