package com.modsen.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {
    public final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    public final String REQUEST_PARAM_MISSING_MESSAGE = "Request param missing";
    public final String INVALID_PARAMETER_TYPE_MESSAGE = "Invalid parameter type";
    public final String DRIVER_ALREADY_EXISTS_MESSAGE = "Driver already exists";
    public final String NOT_FOUND_WITH_ID_MESSAGE = "Driver with id %s was not found";
    public final String INVALID_PAGE_PARAMETERS_MESSAGE =
            "Page number and size must be equal or greater than 1";
    public final String INVALID_SORTING_PARAMETER_MESSAGE =
            "Sorting parameter %s is invalid. Acceptable parameters are: %s";
    public final String DRIVER_WITH_LICENCE_NUMBER_EXISTS_MESSAGE = "Driver with licence number %s already exists";
    public final String DRIVER_WITH_EMAIL_EXISTS_MESSAGE = "Driver with email %s already exists";
    public final String DRIVER_WITH_PHONE_EXISTS_MESSAGE = "Driver with phone %s already exists";
    public final String ACCESS_DENIED_MESSAGE = "Access denied";
}
