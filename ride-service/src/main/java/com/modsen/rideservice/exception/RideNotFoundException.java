package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(long id) {
        super(String.format(RIDE_NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
