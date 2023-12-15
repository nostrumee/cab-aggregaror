package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.InvalidRequestParamException;
import com.modsen.passengerservice.exception.PassengerAlreadyExistsException;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.modsen.passengerservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerServiceImpl passengerService;


    @Test
    void getById_shouldReturnPassenger_whenPassengerExists() {
        var expected = getDefaultPassengerResponse();
        var retrievedPassenger = getDefaultPassenger();

        doReturn(Optional.of(retrievedPassenger))
                .when(passengerRepository)
                .findById(DEFAULT_ID);
        doReturn(expected)
                .when(passengerMapper)
                .fromEntityToResponse(retrievedPassenger);

        var actual = passengerService.getById(DEFAULT_ID);

        verify(passengerRepository).findById(DEFAULT_ID);
        verify(passengerMapper).fromEntityToResponse(retrievedPassenger);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getById_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.getById(DEFAULT_ID)
        );
        verify(passengerRepository).findById(DEFAULT_ID);
    }

    @Test
    void getPassengerPage_shouldReturnPassengerPage_whenValidParamsPassed() {
        var passengerResponseList = getPassengerResponseList();
        var passengersPage = getPassengerPage();
        var retrievedPassengers = getPassengerList();
        var pageRequest = getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        var expected = PassengerPageResponse.builder()
                .passengers(passengerResponseList)
                .pageNumber(PAGE_NUMBER)
                .total(MOCK_TOTAL)
                .build();

        doReturn(passengersPage)
                .when(passengerRepository)
                .findAll(pageRequest);
        doReturn(passengerResponseList)
                .when(passengerMapper)
                .fromEntityListToResponseList(retrievedPassengers);

        var actual = passengerService.getPassengerPage(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        verify(passengerRepository).findAll(pageRequest);
        verify(passengerMapper).fromEntityListToResponseList(retrievedPassengers);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerPage_shouldThrowInvalidRequestParamException_whenInvalidPagePassed() {
        assertThrows(
                InvalidRequestParamException.class,
                () -> passengerService.getPassengerPage(INVALID_PAGE, VALID_SIZE, VALID_ORDER_BY)
        );
    }

    @Test
    void getPassengerPage_shouldThrowInvalidRequestParamException_whenInvalidSizePassed() {
        assertThrows(
                InvalidRequestParamException.class,
                () -> passengerService.getPassengerPage(VALID_PAGE, INVALID_SIZE, VALID_ORDER_BY)
        );
    }

    @Test
    void getPassengerPage_shouldThrowInvalidRequestParamException_whenInvalidOrderByParamPassed() {
        assertThrows(
                InvalidRequestParamException.class,
                () -> passengerService.getPassengerPage(VALID_PAGE, VALID_SIZE, INVALID_ORDER_BY)
        );
    }

    @Test
    void addPassenger_shouldReturnPassengerResponse_whenEmailAndPhoneUnique() {
        var expected = getDefaultPassengerResponse();

        var passengerToSave = getNotSavedPassenger();
        var savedPassenger = getDefaultPassenger();
        var createRequest = getCreatePassengerRequest();

        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);
        doReturn(passengerToSave)
                .when(passengerMapper)
                .fromCreateRequestToEntity(createRequest);
        doReturn(savedPassenger)
                .when(passengerRepository)
                .save(passengerToSave);
        doReturn(expected)
                .when(passengerMapper)
                .fromEntityToResponse(savedPassenger);

        var actual = passengerService.addPassenger(getCreatePassengerRequest());

        verify(passengerRepository).existsByEmail(DEFAULT_EMAIL);
        verify(passengerRepository).existsByPhone(DEFAULT_PHONE);
        verify(passengerRepository).save(passengerToSave);
        verify(passengerMapper).fromCreateRequestToEntity(createRequest);
        verify(passengerMapper).fromEntityToResponse(savedPassenger);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addPassenger_shouldThrowPassengerAlreadyExistsException_whenEmailNotUnique() {
        var createRequest = getCreatePassengerRequest();

        doReturn(true)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);

        assertThrows(
                PassengerAlreadyExistsException.class,
                () -> passengerService.addPassenger(createRequest)
        );

        verify(passengerRepository).existsByEmail(DEFAULT_EMAIL);
        verify(passengerRepository).existsByPhone(DEFAULT_PHONE);
    }

    @Test
    void addPassenger_shouldThrowPassengerAlreadyExistsException_whenPhoneNotUnique() {
        var createRequest = getCreatePassengerRequest();

        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(true)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);

        assertThrows(
                PassengerAlreadyExistsException.class,
                () -> passengerService.addPassenger(createRequest)
        );

        verify(passengerRepository).existsByEmail(DEFAULT_EMAIL);
        verify(passengerRepository).existsByPhone(DEFAULT_PHONE);
    }

    @Test
    void updatePassenger_shouldReturnPassengerResponse_whenPassengerExistsAndDataUnique() {
        var expected = PassengerResponse.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(DEFAULT_RATING)
                .build();
        var passenger = getDefaultPassenger();
        var updateRequest = getUpdatePassengerRequest();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_ID);
        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(updateRequest.email());
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(updateRequest.phone());
        doReturn(expected)
                .when(passengerMapper)
                .fromEntityToResponse(passenger);

        var actual = passengerService.updatePassenger(updateRequest, DEFAULT_ID);

        verify(passengerRepository).findById(DEFAULT_ID);
        verify(passengerRepository).existsByEmail(updateRequest.email());
        verify(passengerRepository).existsByPhone(updateRequest.phone());
        verify(passengerRepository).save(passenger);
        verify(passengerMapper).updateEntityFromUpdateRequest(updateRequest, passenger);
        verify(passengerMapper).fromEntityToResponse(passenger);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updatePassenger_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        var updateRequest = getUpdatePassengerRequest();

        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.updatePassenger(updateRequest, DEFAULT_ID)
        );
    }

    @Test
    void updatePassenger_shouldThrowPassengerAlreadyExistsException_whenEmailNotUnique() {
        var passenger = getDefaultPassenger();
        var updateRequest = getUpdatePassengerRequest();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_ID);
        doReturn(true)
                .when(passengerRepository)
                .existsByEmail(OTHER_EMAIL);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(OTHER_PHONE);

        assertThrows(
                PassengerAlreadyExistsException.class,
                () -> passengerService.updatePassenger(updateRequest, DEFAULT_ID)
        );

        verify(passengerRepository).findById(DEFAULT_ID);
        verify(passengerRepository).existsByEmail(OTHER_EMAIL);
        verify(passengerRepository).existsByPhone(OTHER_PHONE);
    }

    @Test
    void updatePassenger_shouldThrowPassengerAlreadyExistsException_whenPhoneNotUnique() {
        var passenger = getDefaultPassenger();
        var updateRequest = getUpdatePassengerRequest();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_ID);
        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(OTHER_EMAIL);
        doReturn(true)
                .when(passengerRepository)
                .existsByPhone(OTHER_PHONE);

        assertThrows(
                PassengerAlreadyExistsException.class,
                () -> passengerService.updatePassenger(updateRequest, DEFAULT_ID)
        );

        verify(passengerRepository).findById(DEFAULT_ID);
        verify(passengerRepository).existsByEmail(OTHER_EMAIL);
        verify(passengerRepository).existsByPhone(OTHER_PHONE);
    }

    @Test
    void deletePassenger_shouldDeletePassenger_whenPassengerExists() {
        var passenger = getDefaultPassenger();

        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        passengerService.deletePassenger(DEFAULT_ID);

        verify(passengerRepository).findById(DEFAULT_ID);
        verify(passengerRepository).delete(passenger);
    }

    @Test
    void deletePassenger_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.deletePassenger(DEFAULT_ID)
        );
        verify(passengerRepository).findById(DEFAULT_ID);
    }

    @Test
    void updatePassengerRating_shouldUpdateRating_whenPassengerExists() {
        var ratingMessage = getPassengerRatingMessage();

        Passenger passenger = getDefaultPassenger();
        doReturn(Optional.of(passenger))
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        passengerService.updatePassengerRating(ratingMessage);

        verify(passengerRepository).findById(DEFAULT_ID);
        assertEquals(NEW_RATING, passenger.getRating());
    }

    @Test
    void updatePassengerRating_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        var ratingMessage = getPassengerRatingMessage();

        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.updatePassengerRating(ratingMessage)
        );
        verify(passengerRepository).findById(DEFAULT_ID);
    }
}
