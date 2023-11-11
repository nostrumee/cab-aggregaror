package com.modsen.passengerservice.exception;

public class InvalidSortingParameterException extends RuntimeException {

    private static final String INVALID_SORTING_PARAMETER_MESSAGE = "Sorting parameter %s is invalid";

    public InvalidSortingParameterException(String orderBy) {
        super(String.format(INVALID_SORTING_PARAMETER_MESSAGE, orderBy));
    }
}
