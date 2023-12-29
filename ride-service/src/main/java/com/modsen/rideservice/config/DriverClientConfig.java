package com.modsen.rideservice.config;

import com.modsen.rideservice.service.impl.DriverClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class DriverClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DriverClientErrorDecoder();
    }
}
