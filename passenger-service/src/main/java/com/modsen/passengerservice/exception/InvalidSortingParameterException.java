package com.modsen.passengerservice.exception;

public class InvalidSortingParameterException extends RuntimeException {

    private static final String INVALID_SORTING_PARAMETER_MESSAGE =
            "Sorting parameter %s is invalid. Acceptable parameters are: %s";

    public InvalidSortingParameterException(String orderBy, String acceptableParams) {
        super(String.format(INVALID_SORTING_PARAMETER_MESSAGE, orderBy, acceptableParams));
    }
}
