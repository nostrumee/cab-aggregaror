package com.modsen.e2etestsservice.client;

import com.modsen.e2etestsservice.dto.request.CreateRideRequest;
import com.modsen.e2etestsservice.dto.response.DriverResponse;
import com.modsen.e2etestsservice.dto.response.RidePageResponse;
import com.modsen.e2etestsservice.dto.response.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.modsen.e2etestsservice.util.UriPaths.*;

@FeignClient("ride-service")
public interface RideClient {

    @GetMapping(RIDE_SERVICE_BASE_PATH + GET_BY_ID_PATH)
    RideResponse getRideById(@PathVariable("id") long id);

    @PostMapping(RIDE_SERVICE_BASE_PATH)
    RideResponse createRide(@RequestBody CreateRideRequest createRideRequest);

    @GetMapping(RIDE_SERVICE_BASE_PATH + START_RIDE_PATH)
    RideResponse startRide(@PathVariable("id") long id);

    @GetMapping(RIDE_SERVICE_BASE_PATH + FINISH_RIDE_PATH)
    RideResponse finishRide(@PathVariable("id") long id);

    @GetMapping(RIDE_SERVICE_BASE_PATH + GET_PASSENGER_RIDE_HISTORY_PATH)
    RidePageResponse getPassengerRideHistory(@PathVariable("passengerId") long passengerId);

    @GetMapping(RIDE_SERVICE_BASE_PATH + GET_DRIVER_RIDE_HISTORY_PATH)
    RidePageResponse getDriverRideHistory(@PathVariable("driverId") long driverId);
}
