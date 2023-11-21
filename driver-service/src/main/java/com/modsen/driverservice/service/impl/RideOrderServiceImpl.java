package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.RideOrderMessage;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import com.modsen.driverservice.entity.Status;
import com.modsen.driverservice.service.DriverService;
import com.modsen.driverservice.service.RideOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideOrderServiceImpl implements RideOrderService {

    private final DriverService driverService;

    @Override
    public void acceptRideOrder(RideOrderMessage orderMessage) {
        log.info("Searching for a driver to accept a ride order");

        List<DriverResponse> availableDrivers = driverService.getAvailableDrivers();
        if (!availableDrivers.isEmpty()) {
            Random random = new Random();

            DriverResponse driver = availableDrivers.get(random.nextInt(availableDrivers.size()));
            driverService.setDriverStatus(driver.id(), Status.UNAVAILABLE);

            AcceptRideMessage acceptRideMessage = AcceptRideMessage.builder()
                    .rideId(orderMessage.rideId())
                    .driverId(driver.id())
                    .build();

            // TODO: send acceptRideMessage to accept-ride topic
        }
    }
}
