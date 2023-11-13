package com.modsen.passengerservice.exception;

public class InvalidRequestParamException extends RuntimeException {

    public InvalidRequestParamException(String message) {
        super(message);
    }
}
