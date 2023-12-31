package com.modsen.driverservice.config;

import com.modsen.driverservice.dto.message.DriverStatusMessage;
import com.modsen.driverservice.dto.message.DriverRatingMessage;
import com.modsen.driverservice.service.DriverService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class DriverFunctions {

    @Bean
    Consumer<DriverRatingMessage> updateDriverRating(DriverService driverService) {
        return driverService::updateDriverRating;
    }

    @Bean
    Consumer<DriverStatusMessage> updateDriverStatus(DriverService driverService) {
        return message -> driverService.updateDriverStatus(message.driverId(), message.status());
    }
}
