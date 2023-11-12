package com.modsen.driverservice.exception;

public class DriverNotFoundException extends RuntimeException {

    private static final String NOT_FOUND_WITH_ID_MESSAGE = "Driver with id %d was not found";

    public DriverNotFoundException(long id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
