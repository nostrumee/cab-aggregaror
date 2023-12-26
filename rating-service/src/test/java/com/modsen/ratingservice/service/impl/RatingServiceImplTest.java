package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.exception.InvalidRideStatusException;
import com.modsen.ratingservice.exception.RideNotFoundException;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RideService;
import com.modsen.ratingservice.service.SendMessageHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.modsen.ratingservice.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTest {

    @Mock
    private PassengerRatingRepository passengerRatingRepository;

    @Mock
    private DriverRatingRepository driverRatingRepository;

    @Mock
    private RideService rideService;

    @Mock
    private SendMessageHandler sendMessageHandler;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    void ratePassenger_shouldSaveAndUpdatePassengerRating_whenRideExistsAndHasValidStatus() {
        // arrange
        var ratingRequest = getPassengerRatingRequest();
        var rideResponse = getRideResponse(RideStatus.FINISHED);
        var passengerRating = getPassengerRating();
        var ratingMessage = getPassengerRatingMessage();

        doReturn(rideResponse)
                .when(rideService)
                .getRideById(ratingRequest.rideId());
        doReturn(passengerRating)
                .when(ratingMapper)
                .fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        doReturn(UPDATED_RATING)
                .when(passengerRatingRepository)
                .findPassengerRating(rideResponse.passengerId());

        // act
        ratingService.ratePassenger(ratingRequest);

        // assert
        verify(rideService).getRideById(ratingRequest.rideId());
        verify(ratingMapper).fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        verify(passengerRatingRepository).save(passengerRating);
        verify(passengerRatingRepository).findPassengerRating(rideResponse.passengerId());
        verify(sendMessageHandler).handlePassengerRatingMessage(ratingMessage);
    }

    @Test
    void ratePassenger_shouldThrowInvalidRideStatusException_whenRideExistsAndHasInalidStatus() {
        // arrange
        var ratingRequest = getPassengerRatingRequest();
        var rideResponse = getRideResponse(RideStatus.STARTED);
        doReturn(rideResponse)
                .when(rideService)
                .getRideById(ratingRequest.rideId());

        // act and assert
        assertThrows(
                InvalidRideStatusException.class,
                () -> ratingService.ratePassenger(ratingRequest)
        );
        verify(rideService).getRideById(ratingRequest.rideId());
    }

    @Test
    void ratePassenger_shouldThrowRideNotFoundStatusException_whenRideNotExist() {
        // arrange
        var ratingRequest = getPassengerRatingRequest();
        doThrow(RideNotFoundException.class)
                .when(rideService)
                .getRideById(ratingRequest.rideId());

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> ratingService.ratePassenger(ratingRequest)
        );
        verify(rideService).getRideById(ratingRequest.rideId());
    }

    @Test
    void rateDriver_shouldSaveAndUpdatePassengerRating_whenRideExistsAndHasValidStatus() {
        // arrange
        var ratingRequest = getDriverRatingRequest();
        var rideResponse = getRideResponse(RideStatus.FINISHED);
        var driverRating = getDriverRating();
        var ratingMessage = getDriverRatingMessage();

        doReturn(rideResponse)
                .when(rideService)
                .getRideById(ratingRequest.rideId());
        doReturn(driverRating)
                .when(ratingMapper)
                .fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        doReturn(UPDATED_RATING)
                .when(driverRatingRepository)
                .findDriverRating(rideResponse.driverId());

        // act
        ratingService.rateDriver(ratingRequest);

        // assert
        verify(rideService).getRideById(ratingRequest.rideId());
        verify(ratingMapper).fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        verify(driverRatingRepository).save(driverRating);
        verify(driverRatingRepository).findDriverRating(rideResponse.driverId());
        verify(sendMessageHandler).handleDriverRatingMessage(ratingMessage);
    }

    @Test
    void rateDriver_shouldThrowInvalidRideStatusException_whenRideExistsAndHasInalidStatus() {
        // arrange
        var ratingRequest = getDriverRatingRequest();
        var rideResponse = getRideResponse(RideStatus.STARTED);
        doReturn(rideResponse)
                .when(rideService)
                .getRideById(ratingRequest.rideId());

        // act and assert
        assertThrows(
                InvalidRideStatusException.class,
                () -> ratingService.rateDriver(ratingRequest)
        );
        verify(rideService).getRideById(ratingRequest.rideId());
    }

    @Test
    void rateDriver_shouldThrowRideNotFoundStatusException_whenRideNotExist() {
        // arrange
        var ratingRequest = getDriverRatingRequest();
        doThrow(RideNotFoundException.class)
                .when(rideService)
                .getRideById(ratingRequest.rideId());

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> ratingService.rateDriver(ratingRequest)
        );
        verify(rideService).getRideById(ratingRequest.rideId());
    }

}
