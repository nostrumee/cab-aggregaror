package com.modsen.rideservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Schema(description = "Driver profile response")
@Builder
public record DriverResponse(
        @Schema(name = "First name", example = "400")
        @NonNull
        String firstName,

        @Schema(name = "Last name", example = "Doe")
        @NonNull
        String lastName,

        @Schema(name = "Email", example = "johndoe@example.com")
        @NonNull
        String email,

        @Schema(name = "Phone number", example = "123-45-67")
        @NonNull
        String phone
) {
}
