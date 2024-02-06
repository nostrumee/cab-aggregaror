package com.modsen.passengerservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UriPaths {
    public static final String PASSENGER_SERVICE_BASE_PATH = "/api/v1/passengers";
    public static final String GET_PASSENGER_BY_ID_PATH = "/{id}";
    public static final String UPDATE_PASSENGER_BY_ID_PATH = "/{id}";
    public static final String DELETE_PASSENGER_BY_ID_PATH = "/{id}";
}
