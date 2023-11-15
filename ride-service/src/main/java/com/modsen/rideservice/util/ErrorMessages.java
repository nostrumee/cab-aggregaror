package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {
    public final String NOT_FOUND_WITH_ID_MESSAGE = "Ride with id %d was not found";
    public final String NOT_FINISHED_MESSAGE = "Ride with id %d is not finished yet";
    public final String INVALID_PAGE_PARAMETERS_MESSAGE =
            "Page number and size must be equal or greater than 1";
    public final String INVALID_SORTING_PARAMETER_MESSAGE =
            "Sorting parameter %s is invalid. Acceptable parameters are: %s";
}
