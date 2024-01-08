package com.modsen.endtoendtests.dto.response;

public record PassengerResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Double rating
) {
}
