package com.modsen.rideservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Ride response")
@Builder
public record RideResponse(
        @Schema(name = "id", example = "1")
        @NonNull
        Long id,

        @Schema(name = "Passenger id", example = "1")
        @NonNull
        Long passengerId,

        @Schema(name = "Driver id", example = "1")
        @NonNull
        Long driverId,

        @Schema(name = "Start point", example = "Yakuba Kolasa Street, 36")
        @NonNull
        String startPoint,

        @Schema(name = "Destination point", example = "Nesavisimosti Avenue, 4")
        @NonNull
        String destinationPoint,

        @Schema(name = "Created date", example = "2023-11-14 15:53")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @NonNull
        LocalDateTime createdDate,

        @Schema(name = "Accepted date", example = "2023-11-14 15:53")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @NonNull
        LocalDateTime acceptedDate,

        @Schema(name = "Start date", example = "2023-11-14 15:53")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @NonNull
        LocalDateTime startDate,

        @Schema(name = "Finish date", example = "2023-11-14 15:53")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @NonNull
        LocalDateTime finishDate,


        @Schema(name = "Estimated cost", example = "5.3")
        @NonNull
        BigDecimal estimatedCost
) {
}
