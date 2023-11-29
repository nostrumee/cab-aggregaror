package com.modsen.driverservice.config;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.CreateRideMessage;
import com.modsen.driverservice.dto.message.UpdateDriverRatingMessage;
import com.modsen.driverservice.service.DriverService;
import com.modsen.driverservice.service.RideOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class RideFunctions {

    @Bean
    Function<CreateRideMessage, AcceptRideMessage> acceptRideOrder(RideOrderService rideOrderService) {
        return rideOrderService::acceptRideOrder;
    }
}
