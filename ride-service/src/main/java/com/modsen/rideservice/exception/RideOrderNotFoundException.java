package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class RideOrderNotFoundException extends RuntimeException {

    public RideOrderNotFoundException(Long id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
