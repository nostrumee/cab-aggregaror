package com.modsen.passengerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.util.Map;

@Builder
@Schema(description = "Already exists response")
public record AlreadyExistsResponse(
        @Schema(name = "Status", example = "400")
        @NonNull
        Integer status,

        @Schema(name = "Message", example = "Passenger already exists")
        @NonNull
        String message,

        @Schema(name = "Errors")
        @NonNull
        Map<String, String> errors
) {
}
