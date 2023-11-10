package com.modsen.passengerservice.exception;

public class PhoneTakenException extends RuntimeException {

    private static final String PHONE_IS_TAKEN_MESSAGE = "Phone %s is already taken";

    public PhoneTakenException(String phone) {
        super(String.format(PHONE_IS_TAKEN_MESSAGE, phone));
    }
}
