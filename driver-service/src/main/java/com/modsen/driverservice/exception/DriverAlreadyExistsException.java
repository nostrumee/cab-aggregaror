package com.modsen.driverservice.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static com.modsen.driverservice.util.ErrorMessages.DRIVER_ALREADY_EXISTS_MESSAGE;

@Getter
@Setter
public class DriverAlreadyExistsException extends RuntimeException {

    private Map<String, String> errors;

    public DriverAlreadyExistsException(Map<String, String> errors) {
        super(DRIVER_ALREADY_EXISTS_MESSAGE);
        this.errors = errors;
    }
}
