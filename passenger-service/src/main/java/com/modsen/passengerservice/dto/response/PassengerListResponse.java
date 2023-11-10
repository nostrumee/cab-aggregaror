package com.modsen.passengerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Passenger list response")
public record PassengerListResponse (
        @Schema(name = "Passengers")
        List<PassengerResponse> passengers,

        @Schema(name = "Total", example = "100")
        Long total
) {
}
