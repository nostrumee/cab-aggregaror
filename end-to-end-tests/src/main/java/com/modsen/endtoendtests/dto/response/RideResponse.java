package com.modsen.endtoendtests.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.modsen.endtoendtests.annotation.JsonDateTimeFormatter;
import com.modsen.endtoendtests.entity.RideStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RideResponse(
        Long id,
        Long passengerId,
        Long driverId,
        String startPoint,
        String destinationPoint,
        RideStatus status,

        @JsonDateTimeFormatter
        LocalDateTime createdDate,

        @JsonDateTimeFormatter
        LocalDateTime acceptedDate,

        @JsonDateTimeFormatter
        LocalDateTime startDate,

        @JsonDateTimeFormatter
        LocalDateTime finishDate,

        BigDecimal estimatedCost
) {
}
