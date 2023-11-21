package com.modsen.rideservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.util.Map;

@Builder
@Schema(description = "Validation error response")
public record ValidationErrorResponse(
        @Schema(name = "Status", example = "400")
        @NonNull
        Integer status,

        @Schema(name = "Message", example = "Validation failed")
        @NonNull
        String message,

        @Schema(name = "Errors")
        @NonNull
        Map<String, String> errors
) {
}
