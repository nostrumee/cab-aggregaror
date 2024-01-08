package com.modsen.endtoendtests.client;

import com.modsen.endtoendtests.dto.request.DriverRatingRequest;
import com.modsen.endtoendtests.dto.request.PassengerRatingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.modsen.endtoendtests.util.UriPaths.*;

@FeignClient(
        value = "${rating-service.name}",
        path = RATING_SERVICE_BASE_PATH
)
public interface RatingClient {

    @PostMapping(RATE_PASSENGER_PATH)
    void ratePassenger(@RequestBody PassengerRatingRequest passengerRatingRequest);

    @PostMapping(RATE_DRIVER_PATH)
    void rateDriver(@RequestBody DriverRatingRequest passengerRatingRequest);
}
