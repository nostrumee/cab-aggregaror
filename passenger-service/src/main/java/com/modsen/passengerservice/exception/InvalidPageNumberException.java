package com.modsen.passengerservice.exception;

public class InvalidPageNumberException extends RuntimeException {

    private static final String INVALID_PAGE_NUMBER_MESSAGE = "Page number must be equal or greater than 1";

    public InvalidPageNumberException() {
        super(INVALID_PAGE_NUMBER_MESSAGE);
    }
}
