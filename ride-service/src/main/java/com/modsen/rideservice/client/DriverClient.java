package com.modsen.rideservice.client;

import com.modsen.rideservice.dto.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class DriverClient {

    private static final String DRIVERS_ROOT_API = "/api/v1/drivers/";
    private final WebClient webClient;

    public DriverResponse getDriverById(Long id) {
        return webClient
                .get()
                .uri(DRIVERS_ROOT_API + id)
                .retrieve()
                .bodyToMono(DriverResponse.class)
                .block();
    }
}
