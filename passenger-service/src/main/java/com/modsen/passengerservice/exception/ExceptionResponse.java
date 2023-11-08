package com.modsen.passengerservice.exception;

import lombok.*;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class ExceptionResponse {

    private final String message;
    private Map<String, String> errors;
}
