package com.modsen.rideservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntegrationProperties {
    public final String CREATE_RIDE_CHANNEL_NAME = "createRideChannel";
    public final String HANDLE_ACCEPT_RIDE_METHOD_NAME = "handleAcceptRideMessage";
    public final String RIDE_STATUS_CHANNEL_NAME = "rideStatusChannel";
}
