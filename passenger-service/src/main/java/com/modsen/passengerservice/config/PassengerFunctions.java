package com.modsen.passengerservice.config;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.service.PassengerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class PassengerFunctions {

    @Bean
    Consumer<PassengerRatingMessage> updatePassengerRating(PassengerService passengerService) {
        return passengerService::updatePassengerRating;
    }
}
