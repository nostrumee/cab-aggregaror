package com.modsen.endtoendtests.client;

import com.modsen.endtoendtests.dto.response.DriverResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.modsen.endtoendtests.util.UriPaths.*;

@FeignClient(
        value = "${driver-service.name}",
        path = DRIVER_SERVICE_BASE_PATH
)
public interface DriverClient {

    @GetMapping(GET_BY_ID_PATH)
    DriverResponse getDriverById(@PathVariable("id") long id);
}
