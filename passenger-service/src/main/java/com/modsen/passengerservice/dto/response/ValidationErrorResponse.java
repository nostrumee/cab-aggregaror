package com.modsen.passengerservice.dto.response;

import lombok.Builder;
import lombok.NonNull;

import java.util.Map;

@Builder
public record ValidationErrorResponse(
        @NonNull
        Integer status,
        @NonNull
        String message,
        @NonNull
        Map<String, String> errors
) {
}
