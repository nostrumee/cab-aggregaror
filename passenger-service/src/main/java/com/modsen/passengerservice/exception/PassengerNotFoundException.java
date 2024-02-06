package com.modsen.passengerservice.exception;

import java.util.UUID;

import static com.modsen.passengerservice.util.ErrorMessages.*;

public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException(UUID id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
