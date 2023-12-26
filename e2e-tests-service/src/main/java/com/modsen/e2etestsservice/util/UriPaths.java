package com.modsen.e2etestsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UriPaths {
    public static final String DRIVER_SERVICE_BASE_PATH = "/api/v1/drivers";
    public static final String PASSENGER_SERVICE_BASE_PATH = "/api/v1/passengers";
    public static final String RIDE_SERVICE_BASE_PATH = "/api/v1/rides";
    public static final String RATING_SERVICE_BASE_PATH = "/api/v1/rating";

    public static final String GET_BY_ID_PATH = "/{id}";
    public static final String START_RIDE_PATH = "/{id}/start";
    public static final String FINISH_RIDE_PATH = "/{id}/finish";
    public static final String GET_DRIVER_RIDE_HISTORY_PATH = "/driver/{driverId}";
    public static final String GET_PASSENGER_RIDE_HISTORY_PATH = "/passenger/{passengerId}";
    public static final String RATE_PASSENGER_PATH = "/passenger";
    public static final String RATE_DRIVER_PATH = "/driver";
}
