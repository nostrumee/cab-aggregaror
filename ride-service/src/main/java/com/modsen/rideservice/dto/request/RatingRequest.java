package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingRequest(
        @Min(1)
        @Max(5)
        Integer rating,
        String comment
) {
}
