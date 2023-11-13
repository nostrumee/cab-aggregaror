package com.modsen.driverservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessages {
    public static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    public static final String REQUEST_PARAM_MISSING_MESSAGE = "Request param missing";
    public static final String INVALID_PARAMETER_TYPE_MESSAGE = "Invalid parameter type";
    public static final String DRIVER_ALREADY_EXISTS_MESSAGE = "Driver already exists";
    public static final String NOT_FOUND_WITH_ID_MESSAGE = "Driver with id %d was not found";
    public static final String INVALID_PAGE_PARAMETERS_MESSAGE =
            "Page number and size must be equal or greater than 1";
    public static final String INVALID_SORTING_PARAMETER_MESSAGE =
            "Sorting parameter %s is invalid. Acceptable parameters are: %s";
    public static final String DRIVER_WITH_LICENCE_NUMBER_EXISTS_MESSAGE = "Driver with licence number %s already exists";
    public static final String DRIVER_WITH_EMAIL_EXISTS_MESSAGE = "Driver with email %s already exists";
    public static final String DRIVER_WITH_PHONE_EXISTS_MESSAGE = "Driver with phone %s already exists";
}
