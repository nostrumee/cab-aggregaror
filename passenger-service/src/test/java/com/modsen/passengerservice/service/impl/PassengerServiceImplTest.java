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

import java.util.List;
import java.util.Optional;

import static com.modsen.passengerservice.util.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
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

        doReturn(Optional.of(getDefaultPassenger()))
                .when(passengerRepository)
                .findById(DEFAULT_ID);
        doReturn(getDefaultPassengerResponse())
                .when(passengerMapper)
                .fromEntityToResponse(getDefaultPassenger());

        var actual = passengerService.getById(DEFAULT_ID);

        verify(passengerRepository).findById(DEFAULT_ID);
        verify(passengerMapper).fromEntityToResponse(getDefaultPassenger());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getById_shouldThrowPassengerNotFoundException_whenPassengerNotExist() {
        doReturn(Optional.empty())
                .when(passengerRepository)
                .findById(DEFAULT_ID);

        verify(passengerRepository).findById(DEFAULT_ID);

        assertThrows(
                PassengerNotFoundException.class,
                () -> passengerService.getById(DEFAULT_ID)
        );


    }

    @Test
    void getPassengerPage_shouldReturnPassengerPage_whenValidParamsPassed() {
        var expected = PassengerPageResponse.builder()
                .passengers(getPassengerResponseList())
                .pageNumber(PAGE_NUMBER)
                .total(TOTAL)
                .build();

        doReturn(getPassengerPage())
                .when(passengerRepository)
                .findAll(getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY));
        doReturn(getPassengerResponseList())
                .when(passengerMapper)
                .fromEntityListToResponseList(getPassengerList());

        var actual = passengerService.getPassengerPage(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY);

        verify(passengerRepository).findAll(getPageRequest(VALID_PAGE, VALID_SIZE, VALID_ORDER_BY));
        verify(passengerMapper).fromEntityListToResponseList(getPassengerList());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassengerPage_shouldThrowInvalidRequestParamException_whenInvalidParamsPassed() {
        assertThrows(
                InvalidRequestParamException.class,
                () -> passengerService.getPassengerPage(INVALID_PAGE, VALID_SIZE, VALID_ORDER_BY)
        );
        assertThrows(
                InvalidRequestParamException.class,
                () -> passengerService.getPassengerPage(VALID_PAGE, INVALID_SIZE, VALID_ORDER_BY)
        );
        assertThrows(
                InvalidRequestParamException.class,
                () -> passengerService.getPassengerPage(VALID_PAGE, VALID_SIZE, INVALID_ORDER_BY)
        );
    }

    @Test
    void addPassenger_shouldReturnPassengerResponse_whenEmailAndPhoneUnique() {
        var expected = getDefaultPassengerResponse();

        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);
        doReturn(getDefaultPassenger())
                .when(passengerMapper)
                .fromCreateRequestToEntity(getCreatePassengerRequest());
        doReturn(getDefaultPassenger())
                .when(passengerRepository)
                .save(getDefaultPassenger());
        doReturn(getDefaultPassengerResponse())
                .when(passengerMapper)
                .fromEntityToResponse(getDefaultPassenger());

        var actual = passengerService.addPassenger(getCreatePassengerRequest());

        verify(passengerRepository).existsByEmail(DEFAULT_EMAIL);
        verify(passengerRepository).existsByPhone(DEFAULT_PHONE);
        verify(passengerRepository).save(getDefaultPassenger());
        verify(passengerMapper).fromCreateRequestToEntity(getCreatePassengerRequest());
        verify(passengerMapper).fromEntityToResponse(getDefaultPassenger());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addPassenger_shouldThrowPassengerAlreadyExistsException_whenEmailOrPhoneNotUnique() {
        doReturn(true)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);

        assertThrows(
                PassengerAlreadyExistsException.class,
                () -> passengerService.addPassenger(getCreatePassengerRequest())
        );

        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(true)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);

        assertThrows(
                PassengerAlreadyExistsException.class,
                () -> passengerService.addPassenger(getCreatePassengerRequest())
        );

        verify(passengerRepository, times(2)).existsByEmail(DEFAULT_EMAIL);
        verify(passengerRepository, times(2)).existsByPhone(DEFAULT_PHONE);
    }

    @Test
    void updatePassenger_shouldReturnPassengerResponse_whenPassengerExistsAndDataUnique() {
        var expected = getDefaultPassengerResponse();

        doReturn(getDefaultPassenger())
                .when(passengerRepository)
                .findById(DEFAULT_ID);
        doReturn(false)
                .when(passengerRepository)
                .existsByEmail(DEFAULT_EMAIL);
        doReturn(false)
                .when(passengerRepository)
                .existsByPhone(DEFAULT_PHONE);
        doReturn(getDefaultPassenger())
                .when(passengerMapper)
                .fromCreateRequestToEntity(getCreatePassengerRequest());
        doReturn(getDefaultPassenger())
                .when(passengerRepository)
                .save(getDefaultPassenger());
        doReturn(getDefaultPassengerResponse())
                .when(passengerMapper)
                .fromEntityToResponse(getDefaultPassenger());

        var actual = passengerService.addPassenger(getCreatePassengerRequest());

        verify(passengerRepository).existsByEmail(DEFAULT_EMAIL);
        verify(passengerRepository).existsByPhone(DEFAULT_PHONE);
        verify(passengerRepository).save(getDefaultPassenger());
        verify(passengerMapper).fromCreateRequestToEntity(getCreatePassengerRequest());
        verify(passengerMapper).fromEntityToResponse(getDefaultPassenger());

        assertThat(actual).isEqualTo(expected);
    }
}
