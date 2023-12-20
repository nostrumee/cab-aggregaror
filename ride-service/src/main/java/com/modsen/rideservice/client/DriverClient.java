package com.modsen.rideservice.client;

import com.modsen.rideservice.config.DriverClientConfig;
import com.modsen.rideservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "driver-service",
        configuration = DriverClientConfig.class
)
public interface DriverClient {

    @GetMapping("/{id}")
    DriverResponse getDriverById(@PathVariable("id") long id);
}
