package com.modsen.driverservice.exception;

import java.util.UUID;

import static com.modsen.driverservice.util.ErrorMessages.*;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(UUID id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
