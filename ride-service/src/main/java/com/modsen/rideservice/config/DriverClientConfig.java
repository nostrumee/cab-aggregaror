package com.modsen.rideservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DriverClientConfig {

    @Bean
    WebClient webClient(DriverClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(clientProperties.driverServiceUri().toString())
                .build();
    }
}
