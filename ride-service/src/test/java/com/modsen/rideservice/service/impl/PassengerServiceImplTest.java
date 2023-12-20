package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.client.PassengerClient;
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
public class PassengerServiceImplTest {

    @Mock
    private PassengerClient passengerClient;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void getPassengerById_shouldReturnPassengerResponse_whenPassengerExists() {
        var expected = getPassengerResponse();

        doReturn(expected)
                .when(passengerClient)
                .getPassengerById(DEFAULT_ID);

        var actual = passengerService.getPassengerById(DEFAULT_ID);

        assertThat(actual).isEqualTo(expected);
        verify(passengerClient).getPassengerById(DEFAULT_ID);
    }

    @Test
    void getPassengerById_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        doThrow(PassengerNotFoundException.class)
                .when(passengerClient)
                .getPassengerById(DEFAULT_ID);

        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.getPassengerById(DEFAULT_ID)
        );
        verify(passengerClient).getPassengerById(DEFAULT_ID);
    }
}
