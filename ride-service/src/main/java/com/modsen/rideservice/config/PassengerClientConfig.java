package com.modsen.rideservice.config;

import com.modsen.rideservice.service.impl.PassengerClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class PassengerClientConfig {

    @Bean
    public ErrorDecoder passengerClientErrorDecoder() {
        return new PassengerClientErrorDecoder();
    }
}
