package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.CreateRideMessage;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.service.DriverService;
import com.modsen.driverservice.service.RideOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideOrderServiceImpl implements RideOrderService {

    private final DriverService driverService;

    @Override
    @Transactional
    public AcceptRideMessage acceptRideOrder(CreateRideMessage orderMessage) {
        log.info("Searching for a driver to accept a ride order with id {}", orderMessage.rideId());

        List<DriverResponse> availableDrivers = driverService.getAvailableDrivers();
        Long driverId = null;

        if (!availableDrivers.isEmpty()) {
            Random random = new Random();

            DriverResponse driver = availableDrivers.get(random.nextInt(availableDrivers.size()));
            driverService.updateDriverStatus(driver.id(), DriverStatus.UNAVAILABLE);
            driverId = driver.id();
        }

        return AcceptRideMessage.builder()
                .rideId(orderMessage.rideId())
                .driverId(driverId)
                .build();
    }
}
