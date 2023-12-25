package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UriPaths {
    public final String DRIVER_SERVICE_BASE_PATH = "/api/v1/drivers";
    public final String PASSENGER_SERVICE_BASE_PATH = "/api/v1/passengers";
    public final String RIDE_SERVICE_BASE_PATH = "/api/v1/rides";

    public final String GET_BY_ID_PATH = "/{id}";
    public final String START_RIDE_PATH = "/{id}/start";
    public final String FINISH_RIDE_PATH = "/{id}/finish";
    public final String DELETE_BY_ID_PATH = "/{id}";
    public final String GET_DRIVER_RIDE_HISTORY_PATH = "/driver/{driverId}";
    public final String GET_PASSENGER_RIDE_HISTORY_PATH = "/passenger/{passengerId}";
    public final String GET_DRIVER_PROFILE_PATH = "/{rideId}/driver";
}
