package com.modsen.rideservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Schema(description = "Ride page response")
@Builder
public record RidePageResponse(

        @Schema(name = "Rides")
        @NonNull
        List<RideResponse> rides,

        @Schema(name = "Page number", example = "1")
        @NonNull
        Integer pageNumber,

        @Schema(name = "Total", example = "10")
        @NonNull
        Long total
) {
}
