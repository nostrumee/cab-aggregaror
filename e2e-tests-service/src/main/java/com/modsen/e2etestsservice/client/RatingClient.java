package com.modsen.e2etestsservice.client;

import com.modsen.e2etestsservice.dto.request.DriverRatingRequest;
import com.modsen.e2etestsservice.dto.request.PassengerRatingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.modsen.e2etestsservice.util.UriPaths.*;

@FeignClient("rating-service")
public interface RatingClient {

    @PostMapping(RATING_SERVICE_BASE_PATH + RATE_PASSENGER_PATH)
    void ratePassenger(@RequestBody PassengerRatingRequest passengerRatingRequest);

    @PostMapping(RATING_SERVICE_BASE_PATH + RATE_DRIVER_PATH)
    void rateDriver(@RequestBody DriverRatingRequest passengerRatingRequest);
}
