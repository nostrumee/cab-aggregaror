package com.modsen.passengerservice.dto;

public record PassengerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
