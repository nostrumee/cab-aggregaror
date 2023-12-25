package com.modsen.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UriPaths {
    public final String DRIVER_SERVICE_V1_BASE_PATH = "/api/v1/drivers";
    public final String GET_DRIVER_BY_ID_PATH = "/{id}";
    public final String UPDATE_DRIVER_BY_ID_PATH = "/{id}";
    public final String DELETE_DRIVER_BY_ID_PATH = "/{id}";
}
