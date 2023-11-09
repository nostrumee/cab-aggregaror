package com.modsen.passengerservice.dto.response;


import lombok.Builder;
import lombok.NonNull;

@Builder
public record ErrorResponse(
        @NonNull
        Integer status,
        @NonNull
        String message
) {
}
