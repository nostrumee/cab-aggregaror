package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class InvalidRideStatusException extends RuntimeException {

    public InvalidRideStatusException(String statusName) {
        super(String.format(INVALID_RIDE_STATUS_MESSAGE, statusName));
    }
}
