package com.modsen.passengerservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

@Schema(description = "Rating response")
@Builder
public record RatingResponse(
        @Schema(name = "Rating", example = "4.99")
        @NonNull
        Double rating
) {
}
