package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class RideCreateException extends RuntimeException {

    public RideCreateException(String message) {
        super(String.format(RIDE_CREATE_ERROR_MESSAGE, message));
    }
}
