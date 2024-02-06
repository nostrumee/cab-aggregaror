package com.modsen.driverservice.dto.response;

import com.modsen.driverservice.entity.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Schema(description = "Driver response")
@Builder
public record DriverResponse(
        @Schema(name = "id", example = "1")
        @NonNull
        Long id,

        @Schema(name = "externalId", example = "d3849c45-a4f6-4e2a-8289-6b662076fabf")
        @NonNull
        UUID externalId,

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
        String phone,

        @Schema(name = "Rating", example = "4.95")
        @NonNull
        Double rating,

        @Schema(name = "Status", example = "AVAILABLE")
        @NonNull
        DriverStatus status
) {
}
