package com.modsen.passengerservice.dto;

public record CreatePassengerRequest(
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
