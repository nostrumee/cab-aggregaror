package com.modsen.passengerservice.dto;

public record UpdatePassengerRequest(
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
