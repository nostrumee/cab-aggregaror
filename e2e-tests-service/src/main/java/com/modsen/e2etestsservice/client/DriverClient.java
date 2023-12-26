package com.modsen.e2etestsservice.client;

import com.modsen.e2etestsservice.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.modsen.e2etestsservice.util.UriPaths.*;

@FeignClient("driver-service")
public interface DriverClient {

    @GetMapping(DRIVER_SERVICE_BASE_PATH + GET_BY_ID_PATH)
    DriverResponse getDriverById(@PathVariable("id") long id);
}
