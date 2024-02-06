package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.client.DriverClient;
import com.modsen.rideservice.exception.DriverNotFoundException;
import com.modsen.rideservice.exception.PassengerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.modsen.rideservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    private DriverClient driverClient;

    @InjectMocks
    private DriverServiceImpl driverService;

    @Test
    void getDriverById_shouldReturnDriverResponse_whenDriverExists() {
        // arrange
        var expected = getDriverResponse();
        doReturn(expected)
                .when(driverClient)
                .getDriverById(DEFAULT_DRIVER_ID);

        // act
        var actual = driverService.getDriverById(DEFAULT_DRIVER_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(driverClient).getDriverById(DEFAULT_DRIVER_ID);
    }

    @Test
    void getDriverById_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        doThrow(DriverNotFoundException.class)
                .when(driverClient)
                .getDriverById(DEFAULT_DRIVER_ID);

        // act and assert
        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.getDriverById(DEFAULT_DRIVER_ID)
        );
        verify(driverClient).getDriverById(DEFAULT_DRIVER_ID);
    }
}
