package com.modsen.passengerservice.config;

import com.modsen.passengerservice.dto.message.UpdatePassengerRatingMessage;
import com.modsen.passengerservice.service.PassengerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class PassengerFunctions {

    @Bean
    Consumer<UpdatePassengerRatingMessage> updatePassengerRating(PassengerService passengerService) {
        return passengerService::updatePassengerRating;
    }
}
