package com.modsen.driverservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Schema(description = "Driver response")
@Builder
public record DriverResponse(
        @Schema(name = "id", example = "1")
        @NonNull
        Long id,

        @Schema(name = "First name", example = "400")
        @NonNull
        String firstName,

        @Schema(name = "Last name", example = "Doe")
        @NonNull
        String lastName,

        @Schema(name = "Licence number", example = "111222333")
        @NonNull
        String licenceNumber,

        @Schema(name = "Email", example = "johndoe@example.com")
        @NonNull
        String email,

        @Schema(name = "Phone number", example = "123-45-67")
        @NonNull
        String phone
) {
}
