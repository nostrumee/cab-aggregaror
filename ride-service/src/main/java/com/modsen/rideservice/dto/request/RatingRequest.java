package com.modsen.rideservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record RatingRequest(
        @Min(value = 1, message = "{rating.valid}")
        @Max(value = 5, message = "{rating.valid}")
        Integer rating
) {
}
