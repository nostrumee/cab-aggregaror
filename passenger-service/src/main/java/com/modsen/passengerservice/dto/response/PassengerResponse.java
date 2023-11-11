package com.modsen.passengerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Schema(description = "Passenger response")
@Builder
public record PassengerResponse(
        @Schema(name = "id", example = "1")
        @NonNull
        Long id,

        @Schema(name = "First name", example = "John")
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
