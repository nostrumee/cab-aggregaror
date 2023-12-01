package com.modsen.ratingservice.dto.request;

import org.hibernate.validator.constraints.Range;

public record DriverRatingRequest(
        @Range(min = 1, message = "{ride-id.positive}")
        Long rideId,

        @Range(min = 1, max = 5, message = "{rating.valid")
        Integer rating
) {
}
