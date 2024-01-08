package com.modsen.rideservice.client;

import com.modsen.rideservice.config.PassengerClientConfig;
import com.modsen.rideservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.modsen.rideservice.util.UriPaths.*;

@FeignClient(
        value = "${passenger-service.name}",
        path = PASSENGER_SERVICE_BASE_PATH,
        configuration = PassengerClientConfig.class
)
public interface PassengerClient {

    @GetMapping(GET_BY_ID_PATH)
    PassengerResponse getPassengerById(@PathVariable("id") long id);
}
