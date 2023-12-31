package com.modsen.rideservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Schema(description = "Create ride order request")
@Builder
public record CreateRideRequest(
        @Range(min = 1, message = "{passenger-id.positive}")
        Long passengerId,

        @NotBlank(message = "{start-point.not.blank}")
        @Length(max = 255, message = "{start-point.length}")
        String startPoint,

        @NotBlank(message = "{destination-point.not.blank}")
        @Length(max = 255, message = "{destination-point.length}")
        String destinationPoint
) {
}
