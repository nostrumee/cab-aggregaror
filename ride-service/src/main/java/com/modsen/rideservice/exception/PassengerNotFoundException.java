package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException(String message) {
        super(String.format(RIDE_CREATE_ERROR_MESSAGE, message));
    }
}
