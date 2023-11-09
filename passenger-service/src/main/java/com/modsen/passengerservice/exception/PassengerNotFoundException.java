package com.modsen.passengerservice.exception;

public class PassengerNotFoundException extends RuntimeException {

    private static final String NOT_FOUND_WITH_ID_MESSAGE = "Passenger with id %d was not found";

    public PassengerNotFoundException(Long id) {
        super(String.format(NOT_FOUND_WITH_ID_MESSAGE, id));
    }
}
