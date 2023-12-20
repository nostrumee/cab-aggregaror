package com.modsen.rideservice.client;

import com.modsen.rideservice.config.PassengerClientConfig;
import com.modsen.rideservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "passenger-service",
        configuration = PassengerClientConfig.class
)
public interface PassengerClient {

    @GetMapping("/{id}")
    PassengerResponse getPassengerById(@PathVariable("id") long id);
}
