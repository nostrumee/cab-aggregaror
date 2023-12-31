package com.modsen.driverservice.exception;

import static com.modsen.driverservice.util.ErrorMessages.*;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(long id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
