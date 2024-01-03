package com.modsen.ratingservice.dto.request;

import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record DriverRatingRequest(
        @Range(min = 1, message = "{ride-id.valid}")
        Long rideId,

        @Range(min = 1, max = 5, message = "{rating.valid}")
        Integer rating
) {
}
