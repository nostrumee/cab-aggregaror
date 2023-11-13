package com.modsen.passengerservice.exception;

import lombok.Getter;

import java.util.Map;

import static com.modsen.passengerservice.util.ErrorMessages.*;


@Getter
public class PassengerAlreadyExistsException extends RuntimeException {

    private Map<String, String> errors;

    public PassengerAlreadyExistsException(Map<String, String> errors) {
        super(PASSENGER_ALREADY_EXISTS_MESSAGE);
        this.errors = errors;
    }
}
