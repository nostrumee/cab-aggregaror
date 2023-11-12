package com.modsen.driverservice.exception;

public class EmailTakenException extends RuntimeException {

    private static final String EMAIL_IS_TAKEN_MESSAGE = "Email %s is already taken";

    public EmailTakenException(String email) {
        super(String.format(EMAIL_IS_TAKEN_MESSAGE, email));
    }
}
