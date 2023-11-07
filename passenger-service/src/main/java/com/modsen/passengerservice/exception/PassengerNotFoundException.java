package com.modsen.passengerservice.exception;

public class PassengerNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Passenger with id %d was not found";

    public PassengerNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
