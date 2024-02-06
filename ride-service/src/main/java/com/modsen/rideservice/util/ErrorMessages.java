package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {
    public final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    public final String REQUEST_PARAM_MISSING_MESSAGE = "Request param missing";
    public final String INVALID_PARAMETER_TYPE_MESSAGE = "Invalid parameter type";
    public final String RIDE_NOT_FOUND_WITH_ID_MESSAGE = "Ride with id %d was not found";
    public final String INVALID_RIDE_STATUS_MESSAGE = "Invalid ride status. For this action acceptable statuses are: %s";
    public final String INVALID_PAGE_PARAMETERS_MESSAGE =
            "Page number and size must be equal or greater than 1";
    public final String INVALID_SORTING_PARAMETER_MESSAGE =
            "Sorting parameter %s is invalid. Acceptable parameters are: %s";
    public final String RESPONSE_HANDLER_MISSING = "Response handler missing for status code: %d, url: %s";
    public final String DRIVER_SERVICE_UNAVAILABLE = "Driver service unavailable";
    public final String ACCESS_DENIED_MESSAGE = "Access denied";
}
