package com.modsen.rideservice.config;

import com.modsen.rideservice.service.impl.PassengerServiceErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class PassengerClientConfig {

    @Bean
    public ErrorDecoder passengerServiceErrorDecoder() {
        return new PassengerServiceErrorDecoder();
    }
}
