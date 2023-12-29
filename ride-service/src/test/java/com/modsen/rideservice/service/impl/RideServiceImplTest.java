package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.message.CreateRideMessage;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.entity.DriverStatus;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.exception.InvalidRequestParamException;
import com.modsen.rideservice.exception.InvalidRideStatusException;
import com.modsen.rideservice.exception.PassengerNotFoundException;
import com.modsen.rideservice.exception.RideNotFoundException;
import com.modsen.rideservice.mapper.MessageMapper;
import com.modsen.rideservice.mapper.RideMapper;
import com.modsen.rideservice.repository.RideRepository;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.service.PassengerService;
import com.modsen.rideservice.service.SendMessageHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static com.modsen.rideservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private DriverService driverService;

    @Mock
    private PassengerService passengerService;

    @Mock
    private SendMessageHandler sendMessageHandler;

    @InjectMocks
    private RideServiceImpl rideService;

    @Test
    void getRidesPage_shouldReturnRidesPage_whenValidParamsPassed() {
        // arrange
        var rideResponseList = getRideResponseList();
        var ridesPage = getRidePage();
        var retrievedRides = getRideList();
        var pageRequest = getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);
        var expected = RidePageResponse.builder()
                .rides(rideResponseList)
                .pageNumber(VALID_PAGE)
                .total(5L)
                .build();

        doReturn(ridesPage)
                .when(rideRepository)
                .findAll(pageRequest);
        doReturn(rideResponseList)
                .when(rideMapper)
                .fromEntityListToResponseList(retrievedRides);

        // act
        var actual = rideService.getRidesPage(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findAll(pageRequest);
        verify(rideMapper).fromEntityListToResponseList(retrievedRides);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesPage_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        // act and assert
        assertThrows(
                InvalidRequestParamException.class,
                () -> rideService.getRidesPage(page, size, orderBy)
        );
    }

    @Test
    void getRidesByDriverId_shouldReturnDriverFinishedRides_whenValidParamsPassed() {
        // arrange
        var rideResponseList = getRidesHistoryResponseList();
        var ridesPage = getRidesHistoryPage();
        var retrievedRides = getRidesHistoryList();
        var pageRequest = getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);
        var expected = RidePageResponse.builder()
                .rides(rideResponseList)
                .pageNumber(VALID_PAGE)
                .total(1L)
                .build();

        doReturn(ridesPage)
                .when(rideRepository)
                .findAllByDriverIdAndStatus(DEFAULT_ID, RideStatus.FINISHED, pageRequest);
        doReturn(rideResponseList)
                .when(rideMapper)
                .fromEntityListToResponseList(retrievedRides);

        // act
        var actual = rideService.getRidesByDriverId(DEFAULT_ID, VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findAllByDriverIdAndStatus(DEFAULT_ID, RideStatus.FINISHED, pageRequest);
        verify(rideMapper).fromEntityListToResponseList(retrievedRides);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesByDriverId_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        // act and assert
        assertThrows(
                InvalidRequestParamException.class,
                () -> rideService.getRidesByDriverId(DEFAULT_ID, page, size, orderBy)
        );
    }

    @Test
    void getRidesByPassengerId_shouldReturnPassengerFinishedRides_whenValidParamsPassed() {
        // arrange
        var rideResponseList = getRidesHistoryResponseList();
        var ridesPage = getRidesHistoryPage();
        var retrievedRides = getRidesHistoryList();
        var pageRequest = getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);
        var expected = RidePageResponse.builder()
                .rides(rideResponseList)
                .pageNumber(VALID_PAGE)
                .total(1L)
                .build();

        doReturn(ridesPage)
                .when(rideRepository)
                .findAllByPassengerIdAndStatus(DEFAULT_ID, RideStatus.FINISHED, pageRequest);
        doReturn(rideResponseList)
                .when(rideMapper)
                .fromEntityListToResponseList(retrievedRides);

        // act
        var actual = rideService.getRidesByPassengerId(DEFAULT_ID, VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findAllByPassengerIdAndStatus(DEFAULT_ID, RideStatus.FINISHED, pageRequest);
        verify(rideMapper).fromEntityListToResponseList(retrievedRides);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesByPassengerId_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        // act and assert
        assertThrows(
                InvalidRequestParamException.class,
                () -> rideService.getRidesByPassengerId(DEFAULT_ID, page, size, orderBy)
        );
    }

    @Test
    void getById_shouldReturnRideResponse_whenRideExists() {
        // arrange
        var expected = getFinishedRideResponse();
        var retrievedRide = getFinishedRide();

        doReturn(Optional.of(retrievedRide))
                .when(rideRepository)
                .findById(FINISHED_RIDE_ID);
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(retrievedRide);

        // act
        var actual = rideService.getById(FINISHED_RIDE_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findById(FINISHED_RIDE_ID);
        verify(rideMapper).fromEntityToResponse(retrievedRide);
    }

    @Test
    void getById_shouldThrowRideNotFoundException_whenRideNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.getById(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void createRide_shouldReturnCreatedRideResponse_whenPassengerExists() {
        // arrange
        var expected = getCreatedRideResponse();
        var rideToSave = getNotSavedRide();
        var savedRide = getCreatedRide();
        var createRequest = getCreateRideRequest();
        var createRideMessage = new CreateRideMessage(DEFAULT_ID);

        doReturn(rideToSave)
                .when(rideMapper)
                .fromCreateRequestToEntity(createRequest);
        doReturn(savedRide)
                .when(rideRepository)
                .save(rideToSave);
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(savedRide);

        // act
        var actual = rideService.createRide(createRequest);

        // assert
        assertThat(actual).isEqualTo(expected);
        assertThat(rideToSave.getStatus()).isEqualTo(RideStatus.CREATED);
        assertThat(rideToSave.getCreatedDate()).isNotNull();
        assertThat(rideToSave.getEstimatedCost()).isNotNull();

        verify(passengerService).getPassengerById(createRequest.passengerId());
        verify(rideRepository).save(rideToSave);
        verify(rideMapper).fromCreateRequestToEntity(createRequest);
        verify(rideMapper).fromEntityToResponse(savedRide);
        verify(sendMessageHandler).handleCreateRideMessage(createRideMessage);
    }

    @Test
    void createRide_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        // arrange
        var createRequest = getCreateRideRequest();
        doThrow(PassengerNotFoundException.class)
                .when(passengerService)
                .getPassengerById(createRequest.passengerId());

        // act and assert
        assertThrows(
                PassengerNotFoundException.class,
                () -> rideService.createRide(createRequest)
        );
    }

    @Test
    void deleteRide_shouldDeleteRide_whenRideExists() {
        // arrange
        var ride = getFinishedRide();
        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(FINISHED_RIDE_ID);

        // act
        rideService.deleteRide(FINISHED_RIDE_ID);

        // assert
        verify(rideRepository).findById(FINISHED_RIDE_ID);
        verify(rideRepository).delete(ride);
    }

    @Test
    void deleteRide_shouldThrowRideNotFoundException_whenRideNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.deleteRide(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void acceptRide_shouldMakeRideAccepted_whenDriverAssignedAndRideExists() {
        // arrange
        var ride = getCreatedRide();
        var passengerResponse = getPassengerResponse();
        var acceptRideMessage = getAcceptRideMessage(DEFAULT_ID);
        var rideStatusMessage = getRideStatusMessage(RideStatus.ACCEPTED);

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(CREATED_RIDE_ID);
        doReturn(passengerResponse)
                .when(passengerService)
                .getPassengerById(ride.getPassengerId());
        doReturn(rideStatusMessage)
                .when(messageMapper)
                .fromRideAndPassengerResponse(ride, passengerResponse);

        // act
        rideService.acceptRide(acceptRideMessage);

        // assert
        assertThat(ride.getDriverId()).isEqualTo(acceptRideMessage.driverId());
        assertThat(ride.getStatus()).isEqualTo(RideStatus.ACCEPTED);
        assertThat(ride.getAcceptedDate()).isNotNull();

        verify(rideRepository).findById(acceptRideMessage.rideId());
        verify(passengerService).getPassengerById(ride.getPassengerId());
        verify(rideRepository).save(ride);
        verify(messageMapper).fromRideAndPassengerResponse(ride, passengerResponse);
        verify(sendMessageHandler).handleRideStatusMessage(rideStatusMessage);
    }

    @Test
    void acceptRide_shouldMakeRideRejected_whenDriverNotAssignedAndRideExists() {
        // arrange
        var ride = getCreatedRide();
        var passengerResponse = getPassengerResponse();
        var acceptRideMessage = getAcceptRideMessage(null);
        var rideStatusMessage = getRideStatusMessage(RideStatus.REJECTED);

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(CREATED_RIDE_ID);
        doReturn(passengerResponse)
                .when(passengerService)
                .getPassengerById(ride.getPassengerId());
        doReturn(rideStatusMessage)
                .when(messageMapper)
                .fromRideAndPassengerResponse(ride, passengerResponse);

        // act
        rideService.acceptRide(acceptRideMessage);

        // assert
        assertThat(ride.getDriverId()).isNull();
        assertThat(ride.getStatus()).isEqualTo(RideStatus.REJECTED);
        assertThat(ride.getAcceptedDate()).isNull();

        verify(rideRepository).findById(acceptRideMessage.rideId());
        verify(passengerService).getPassengerById(ride.getPassengerId());
        verify(rideRepository).save(ride);
        verify(messageMapper).fromRideAndPassengerResponse(ride, passengerResponse);
        verify(sendMessageHandler).handleRideStatusMessage(rideStatusMessage);
    }

    @Test
    void acceptRide_shouldThrowRideNotFoundException_whenRideNotExist() {
        // arrange
        var acceptRideMessage = getAcceptRideMessage(DEFAULT_ID);
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.acceptRide(acceptRideMessage)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void startRide_shouldReturnStartedRideResponse_whenRideExistsAndHasAcceptedStatus() {
        // arrange
        var expected = getStartedRideResponse();
        var ride = getAcceptedRide();
        var passengerResponse = getPassengerResponse();
        var rideStatusMessage = getRideStatusMessage(RideStatus.STARTED);

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(DEFAULT_ID);
        doReturn(passengerResponse)
                .when(passengerService)
                .getPassengerById(ride.getPassengerId());
        doReturn(ride)
                .when(rideRepository)
                .save(ride);
        doReturn(rideStatusMessage)
                .when(messageMapper)
                .fromRideAndPassengerResponse(ride, passengerResponse);
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(ride);

        // act
        var actual = rideService.startRide(DEFAULT_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        assertThat(ride.getStatus()).isEqualTo(RideStatus.STARTED);
        assertThat(ride.getStartDate()).isNotNull();

        verify(rideRepository).findById(DEFAULT_ID);
        verify(passengerService).getPassengerById(ride.getPassengerId());
        verify(rideRepository).save(ride);
        verify(messageMapper).fromRideAndPassengerResponse(ride, passengerResponse);
        verify(sendMessageHandler).handleRideStatusMessage(rideStatusMessage);
        verify(rideMapper).fromEntityToResponse(ride);
    }

    @Test
    void startRide_shouldThrowInvalidRideStatusException_whenRideExistsAndHasInvalidStatus() {
        // arrange
        var ride = getStartedRide();
        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                InvalidRideStatusException.class,
                () -> rideService.startRide(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void startRide_shouldThrowRideNotFoundException_whenRideNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act an assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.startRide(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void finishRide_shouldReturnFinishedRideResponse_whenRideExistsAndHasAcceptedStatus() {
        // arrange
        var expected = getFinishedRideResponse();
        var ride = getStartedRide();
        var passengerResponse = getPassengerResponse();
        var rideStatusMessage = getRideStatusMessage(RideStatus.FINISHED);
        var driverStatusMessage = getDriverStatusMessage(DriverStatus.AVAILABLE);

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(DEFAULT_ID);
        doReturn(passengerResponse)
                .when(passengerService)
                .getPassengerById(ride.getPassengerId());
        doReturn(ride)
                .when(rideRepository)
                .save(ride);
        doReturn(rideStatusMessage)
                .when(messageMapper)
                .fromRideAndPassengerResponse(ride, passengerResponse);
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(ride);

        // act
        var actual = rideService.finishRide(DEFAULT_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        assertThat(ride.getStatus()).isEqualTo(RideStatus.FINISHED);
        assertThat(ride.getFinishDate()).isNotNull();

        verify(rideRepository).findById(DEFAULT_ID);
        verify(passengerService).getPassengerById(ride.getPassengerId());
        verify(rideRepository).save(ride);
        verify(messageMapper).fromRideAndPassengerResponse(ride, passengerResponse);
        verify(sendMessageHandler).handleRideStatusMessage(rideStatusMessage);
        verify(sendMessageHandler).handleDriverStatusMessage(driverStatusMessage);
        verify(rideMapper).fromEntityToResponse(ride);
    }

    @Test
    void finishRide_shouldThrowInvalidRideStatusException_whenRideExistsAndHasInvalidStatus() {
        // arrange
        var ride = getFinishedRide();
        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                InvalidRideStatusException.class,
                () -> rideService.finishRide(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void finishRide_shouldThrowRideNotFoundException_whenRideNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.finishRide(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void getDriverProfile_shouldReturnDriverResponse_whenRideExistsAndHasValidStatus() {
        // arrange
        var ride = getFinishedRide();
        var expected = getDriverResponse();

        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(DEFAULT_ID);
        doReturn(expected)
                .when(driverService)
                .getDriverById(ride.getDriverId());

        // act
        var actual = rideService.getDriverProfile(DEFAULT_ID);

        // assert
        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findById(DEFAULT_ID);
        verify(driverService).getDriverById(ride.getDriverId());
    }

    @Test
    void getDriverProfile_shouldThrowInvalidRideStatusException_whenRideExistsAndHasInvalidStatus() {
        // arrange
        var ride = getRejectedRide();
        doReturn(Optional.of(ride))
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                InvalidRideStatusException.class,
                () -> rideService.getDriverProfile(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void getDriverProfile_shouldThrowRideNotFoundException_whenRideNotExist() {
        // arrange
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        // act and assert
        assertThrows(
                RideNotFoundException.class,
                () -> rideService.getDriverProfile(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    private static Stream<Arguments> getInvalidParamsForGetRidesPageTest() {
        return Stream.of(
                Arguments.of(INVALID_PAGE, VALID_SIZE, VALID_ORDER_BY),
                Arguments.of(VALID_PAGE, INVALID_SIZE, VALID_ORDER_BY),
                Arguments.of(VALID_PAGE, VALID_SIZE, INVALID_ORDER_BY)
        );
    }
}
