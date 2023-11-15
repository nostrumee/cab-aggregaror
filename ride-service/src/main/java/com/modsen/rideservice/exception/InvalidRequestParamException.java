package com.modsen.rideservice.exception;

public class InvalidRequestParamException extends RuntimeException {

    public InvalidRequestParamException(String message) {
        super(message);
    }
}
