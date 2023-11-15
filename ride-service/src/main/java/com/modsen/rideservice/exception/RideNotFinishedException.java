package com.modsen.rideservice.exception;

import static com.modsen.rideservice.util.ErrorMessages.*;

public class RideNotFinishedException extends RuntimeException{

    public RideNotFinishedException(long id) {
        super(String.format(NOT_FINISHED_MESSAGE, id));
    }
}
