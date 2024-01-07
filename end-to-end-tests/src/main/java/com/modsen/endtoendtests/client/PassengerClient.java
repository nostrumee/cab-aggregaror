package com.modsen.endtoendtests.client;

import com.modsen.endtoendtests.dto.response.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.modsen.endtoendtests.util.UriPaths.GET_BY_ID_PATH;
import static com.modsen.endtoendtests.util.UriPaths.PASSENGER_SERVICE_BASE_PATH;

@FeignClient("passenger-service")
public interface PassengerClient {

    @GetMapping(PASSENGER_SERVICE_BASE_PATH + GET_BY_ID_PATH)
    PassengerResponse getPassengerById(@PathVariable("id") long id);
}
