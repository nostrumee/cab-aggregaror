package com.modsen.ratingservice.client;

import com.modsen.ratingservice.config.RideClientConfig;
import com.modsen.ratingservice.dto.response.RideResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "ride",
        configuration = RideClientConfig.class
)
public interface RideClient {

    @GetMapping("/{id}")
    RideResponse getRide(@PathVariable("id") long id);
}
