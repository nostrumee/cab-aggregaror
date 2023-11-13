package com.modsen.passengerservice.exception;

import static com.modsen.passengerservice.util.ErrorMessages.*;

public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException(Long id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
