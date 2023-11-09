package com.modsen.passengerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Passenger list response")
public record PassengerListResponse (
        @Schema(name = "Passengers")
        List<PassengerResponse> passengers
) {
}
