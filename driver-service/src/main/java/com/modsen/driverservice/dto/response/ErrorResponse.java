package com.modsen.driverservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Builder
@Schema(description = "Error response")
public record ErrorResponse(
        @Schema(name = "Status", example = "404")
        @NonNull
        Integer status,

        @Schema(name = "Message", example = "Not found")
        @NonNull
        String message
) {
}
