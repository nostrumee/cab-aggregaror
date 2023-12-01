package com.modsen.ratingservice.config;

import com.modsen.ratingservice.service.impl.RideClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RideClientConfig {

    @Bean
    public ErrorDecoder rideClientErrorDecoder() {
        return new RideClientErrorDecoder();
    }
}
