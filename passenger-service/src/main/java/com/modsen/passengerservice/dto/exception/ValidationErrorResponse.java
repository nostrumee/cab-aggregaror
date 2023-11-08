package com.modsen.passengerservice.dto.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class ValidationErrorResponse extends ExceptionResponse {

    private Map<String, String> errors;

    public ValidationErrorResponse(Integer status, String message) {
        super(status, message);
    }
}
