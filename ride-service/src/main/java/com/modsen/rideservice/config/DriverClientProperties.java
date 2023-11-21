package com.modsen.rideservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "driver-client")
public record DriverClientProperties(
        URI driverServiceUri
) {
}
