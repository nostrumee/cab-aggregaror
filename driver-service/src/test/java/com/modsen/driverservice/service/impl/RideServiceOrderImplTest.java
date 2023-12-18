package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.message.CreateRideMessage;
import com.modsen.driverservice.entity.DriverStatus;
import com.modsen.driverservice.service.DriverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.modsen.driverservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RideServiceOrderImplTest {

    @Mock
    private DriverService driverService;

    @InjectMocks
    private RideOrderServiceImpl rideOrderService;

    @Test
    void acceptRideOrder_shouldReturnAcceptRideMessageWithDriverId_whenThereAreAvailableDrivers() {
        var expected = getAcceptRideMessage();
        var availableDrivers = List.of(getDefaultDriverResponse());

        doReturn(availableDrivers)
                .when(driverService)
                .getAvailableDrivers();

        var actual = rideOrderService.acceptRideOrder(new CreateRideMessage(DEFAULT_ID));

        assertThat(actual.rideId()).isEqualTo(expected.rideId());
        verify(driverService).getAvailableDrivers();
        verify(driverService).updateDriverStatus(DEFAULT_ID, DriverStatus.UNAVAILABLE);
    }
}
