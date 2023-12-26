package com.modsen.e2etestsservice.client;

import com.modsen.e2etestsservice.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.modsen.e2etestsservice.util.UriPaths.GET_BY_ID_PATH;
import static com.modsen.e2etestsservice.util.UriPaths.PASSENGER_SERVICE_BASE_PATH;

@FeignClient("passenger-service")
public interface PassengerClient {

    @GetMapping(PASSENGER_SERVICE_BASE_PATH + GET_BY_ID_PATH)
    PassengerResponse getPassengerById(@PathVariable("id") long id);
}
