package com.modsen.rideservice.service.impl;

import com.modsen.rideservice.dto.message.CreateRideMessage;
import com.modsen.rideservice.dto.response.RidePageResponse;
import com.modsen.rideservice.entity.RideStatus;
import com.modsen.rideservice.exception.InvalidRequestParamException;
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

        var actual = rideService.getRidesPage(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findAll(pageRequest);
        verify(rideMapper).fromEntityListToResponseList(retrievedRides);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesPage_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        assertThrows(
                InvalidRequestParamException.class,
                () -> rideService.getRidesPage(page, size, orderBy)
        );
    }

    @Test
    void getRidesByDriverId_shouldReturnDriverFinishedRides_whenValidParamsPassed() {
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

        var actual = rideService.getRidesByDriverId(DEFAULT_ID, VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findAllByDriverIdAndStatus(DEFAULT_ID, RideStatus.FINISHED, pageRequest);
        verify(rideMapper).fromEntityListToResponseList(retrievedRides);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesByDriverId_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        assertThrows(
                InvalidRequestParamException.class,
                () -> rideService.getRidesByDriverId(DEFAULT_ID, page, size, orderBy)
        );
    }

    @Test
    void getRidesByPassengerId_shouldReturnPassengerFinishedRides_whenValidParamsPassed() {
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

        var actual = rideService.getRidesByPassengerId(DEFAULT_ID, VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findAllByPassengerIdAndStatus(DEFAULT_ID, RideStatus.FINISHED, pageRequest);
        verify(rideMapper).fromEntityListToResponseList(retrievedRides);
    }

    @ParameterizedTest
    @MethodSource("getInvalidParamsForGetRidesPageTest")
    void getRidesByPassengerId_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed(int page, int size, String orderBy) {
        assertThrows(
                InvalidRequestParamException.class,
                () -> rideService.getRidesByPassengerId(DEFAULT_ID, page, size, orderBy)
        );
    }

    @Test
    void getById_shouldReturnRideResponse_whenRideExists() {
        var expected = getFinishedRideResponse();
        var retrievedRide = getFinishedRide();

        doReturn(Optional.of(retrievedRide))
                .when(rideRepository)
                .findById(FINISHED_RIDE_ID);
        doReturn(expected)
                .when(rideMapper)
                .fromEntityToResponse(retrievedRide);

        var actual = rideService.getById(FINISHED_RIDE_ID);

        assertThat(actual).isEqualTo(expected);
        verify(rideRepository).findById(FINISHED_RIDE_ID);
        verify(rideMapper).fromEntityToResponse(retrievedRide);
    }

    @Test
    void getById_shouldThrowRideNotFoundException_whenRideNotExist() {
        doReturn(Optional.empty())
                .when(rideRepository)
                .findById(DEFAULT_ID);

        assertThrows(
                RideNotFoundException.class,
                () -> rideService.getById(DEFAULT_ID)
        );
        verify(rideRepository).findById(DEFAULT_ID);
    }

    @Test
    void createRide_shouldReturnCreatedRideResponse_whenPassengerExists() {
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

        var actual = rideService.createRide(createRequest);

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
        var createRequest = getCreateRideRequest();

        doThrow(PassengerNotFoundException.class)
                .when(passengerService)
                .getPassengerById(createRequest.passengerId());

        assertThrows(
                PassengerNotFoundException.class,
                () -> rideService.createRide(createRequest)
        );
    }

    private static Stream<Arguments> getInvalidParamsForGetRidesPageTest() {
        return Stream.of(
                Arguments.of(INVALID_PAGE, VALID_SIZE, VALID_ORDER_BY),
                Arguments.of(VALID_PAGE, INVALID_SIZE, VALID_ORDER_BY),
                Arguments.of(VALID_PAGE, VALID_SIZE, INVALID_ORDER_BY)
        );
    }
}
