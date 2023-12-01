package com.modsen.ratingservice.exception;

import static com.modsen.ratingservice.util.ErrorMessages.*;

public class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(String message) {
        super(String.format(RATING_CREATE_ERROR_MESSAGE, message));
    }
}
