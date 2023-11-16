package com.modsen.ratingservice.dto;

import lombok.Builder;

@Builder
public record RatingResponse(
        Double rating
) {
}
