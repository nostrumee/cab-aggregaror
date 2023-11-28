package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class NoAvailableDriversException extends RuntimeException{

    public NoAvailableDriversException() {
        super(NO_AVAILABLE_DRIVERS_MESSAGE);
    }
}
