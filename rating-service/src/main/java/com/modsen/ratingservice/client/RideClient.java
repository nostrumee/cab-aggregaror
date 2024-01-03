package com.modsen.ratingservice.client;

import com.modsen.ratingservice.config.RideClientConfig;
import com.modsen.ratingservice.dto.response.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.modsen.ratingservice.util.UriPaths.GET_BY_ID_PATH;
import static com.modsen.ratingservice.util.UriPaths.RIDE_SERVICE_BASE_PATH;

@FeignClient(
        value = "ride-service",
        configuration = RideClientConfig.class
)
public interface RideClient {

    @GetMapping(RIDE_SERVICE_BASE_PATH + GET_BY_ID_PATH)
    RideResponse getRideById(@PathVariable("id") long id);
}
