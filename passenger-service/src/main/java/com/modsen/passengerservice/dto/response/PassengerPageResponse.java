package com.modsen.passengerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(description = "Passenger page response")
public record PassengerPageResponse(
        @Schema(name = "Passengers")
        List<PassengerResponse> passengers,

        @Schema(name = "Page number", example = "1")
        Integer pageNumber,

        @Schema(name = "Total", example = "100")
        Long total
) {
}
