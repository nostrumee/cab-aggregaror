package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.client.RideClient;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.exception.RideNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.modsen.ratingservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RideServiceImplTest {

    @Mock
    private RideClient rideClient;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void getRideById_shouldReturnRideResponse_whenRideExists() {
        // arrange
        var expected = getRideResponse(RideStatus.FINISHED);
        doReturn(expected)
                .when(rideClient)
                .getRideById(DEFAULT_ID);

        // act
        var actual = rideService.getRideById(DEFAULT_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(rideClient).getRideById(DEFAULT_ID);
    }

    @Test
    void getDriverById_shouldThrowDriverNotFoundException_whenDriverNotExist() {
        // arrange
        doThrow(RideNotFoundException.class)
                .when(rideClient)
                .getRideById(DEFAULT_ID);

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.getRideById(DEFAULT_ID)
        );
        verify(rideClient).getRideById(DEFAULT_ID);
    }

}
