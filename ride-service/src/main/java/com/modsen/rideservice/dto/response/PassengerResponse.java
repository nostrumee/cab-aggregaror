package com.modsen.rideservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PassengerResponse(

        @JsonProperty("externalId")
        UUID id,
        String firstName,
        String email
) {
}
