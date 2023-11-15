package com.modsen.rideservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RidePageResponse(
        List<RideResponse> rides,
        Integer pageNumber,
        Long total
) {
}
