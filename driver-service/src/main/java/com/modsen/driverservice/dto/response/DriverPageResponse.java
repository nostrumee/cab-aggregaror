package com.modsen.driverservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Schema(description = "Driver page response")
@Builder
public record DriverPageResponse(
        @Schema(name = "Drivers")
        @NonNull
        List<DriverResponse> drivers,

        @Schema(name = "Page number", example = "1")
        @NonNull
        Integer pageNumber,

        @Schema(name = "Total", example = "100")
        @NonNull
        Long total
) {
}
