package com.modsen.rideservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RideOrderResponse(
        Long id,

        Long passengerId,

        Long driverId,

        String startPoint,

        String destinationPoint,

        LocalDateTime createdDate,

        LocalDateTime acceptedDate,

        LocalDateTime startDate,

        LocalDateTime finishDate,

        BigDecimal estimatedCost
) {
}
