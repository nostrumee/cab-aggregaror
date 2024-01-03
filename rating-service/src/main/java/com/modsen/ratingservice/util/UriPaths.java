package com.modsen.ratingservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UriPaths {
    public static final String RATING_SERVICE_BASE_PATH = "/api/v1/rating";
    public static final String RIDE_SERVICE_BASE_PATH = "/api/v1/rides";
    public static final String RATE_PASSENGER_PATH = "/passenger";
    public static final String RATE_DRIVER_PATH = "/driver";
    public static final String GET_BY_ID_PATH = "/{id}";
}
