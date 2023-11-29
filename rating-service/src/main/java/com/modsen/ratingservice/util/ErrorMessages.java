package com.modsen.ratingservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {
    public final String RATING_CREATE_ERROR_MESSAGE = "Error creating rating: %s";
    public final String RESPONSE_HANDLER_MISSING = "Response handler missing for status code: %d, url: %s";
    public final String INVALID_RIDE_STATUS_MESSAGE = "Invalid ride status. For this action ride status must be %s";
}
