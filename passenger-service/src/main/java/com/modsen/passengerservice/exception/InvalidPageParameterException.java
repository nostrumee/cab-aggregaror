package com.modsen.passengerservice.exception;

public class InvalidPageParameterException extends RuntimeException {

    private static final String INVALID_PAGE_PARAMETERS_MESSAGE = "Page number and size must be equal or greater than 1";

    public InvalidPageParameterException() {
        super(INVALID_PAGE_PARAMETERS_MESSAGE);
    }
}
