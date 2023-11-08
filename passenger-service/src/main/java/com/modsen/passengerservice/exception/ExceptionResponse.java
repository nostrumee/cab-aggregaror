package com.modsen.passengerservice.exception;

import java.util.Map;


public record ExceptionResponse (
        String message,
        Map<String, String> errors
) {
    public ExceptionResponse(String message) {
        this(message, null);
    }
}
