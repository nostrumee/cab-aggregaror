package com.modsen.e2etestsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {
    public static final String DRIVER_SERVICE_BASE_PATH = "/api/v1/drivers";
    public static final String PASSENGER_SERVICE_BASE_PATH = "/api/v1/passengers";
    public static final String RIDE_SERVICE_BASE_PATH = "/api/v1/rides";
    public static final String RATING_SERVICE_BASE_PATH = "/api/v1/rating";
}
