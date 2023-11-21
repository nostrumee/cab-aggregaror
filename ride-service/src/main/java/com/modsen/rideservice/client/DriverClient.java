package com.modsen.rideservice.client;

import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static com.modsen.rideservice.util.UriPaths.*;

@Component
@RequiredArgsConstructor
public class DriverClient {

    private final WebClient webClient;

    public DriverResponse getDriverById(Long id) {
        return webClient
                .get()
                .uri(DRIVERS_ROOT_URI + id)
                .retrieve()
                .bodyToMono(DriverResponse.class)
                .block();
    }
}
