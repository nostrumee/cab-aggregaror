package com.modsen.rideservice.dto.response;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record RideResponse(
        @NonNull
        Long id,

        @NonNull
        Long passengerId,

        @NonNull
        Long driverId,

        @NonNull
        String startPoint,

        @NonNull
        String destinationPoint,

        @NonNull
        LocalDateTime createdDate,

        @NonNull
        LocalDateTime acceptedDate,

        @NonNull
        LocalDateTime startDate,

        @NonNull
        LocalDateTime finishDate,

        @NonNull
        BigDecimal estimatedCost
) {
}
