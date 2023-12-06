package com.modsen.passengerservice.util;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class TestUtils {
    public final long DEFAULT_ID = 1L;
    public final String DEFAULT_FIRST_NAME = "John";
    public final String DEFAULT_LAST_NAME = "Doe";
    public final String DEFAULT_EMAIL = "johndoe@example.com";
    public final String DEFAULT_PHONE = "123-45-67";
    public final double DEFAULT_RATING = 5.0;

    public final long OTHER_ID = 1L;
    public final String OTHER_FIRST_NAME = "Alice";
    public final String OTHER_LAST_NAME = "Smith";
    public final String OTHER_EMAIL = "alicesmith@example.com";
    public final String OTHER_PHONE = "674-94-39";
    public final double OTHER_RATING = 4.65;

    public final int PAGE_NUMBER = 1;
    public final long TOTAL = 2L;

    public final int VALID_PAGE = 1;
    public final int VALID_SIZE = 2;
    public final String VALID_ORDER_BY = "firstName";

    public final int INVALID_PAGE = -1;
    public final int INVALID_SIZE = -1;
    public final String INVALID_ORDER_BY = "invalid";

    public Passenger getDefaultPassenger() {
        return Passenger.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public Passenger getOtherPassenger() {
        return Passenger.builder()
                .id(OTHER_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();
    }

    public Passenger getNotSavedPassenger() {
        return Passenger.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public PassengerResponse getDefaultPassengerResponse() {
        return PassengerResponse.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(DEFAULT_RATING)
                .build();
    }

    public PassengerResponse getOtherPassengerResponse() {
        return PassengerResponse.builder()
                .id(OTHER_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(OTHER_RATING)
                .build();
    }

    public PageRequest getPageRequest(int page, int size, String orderBy) {
        return PageRequest.of(page - 1, size, Sort.by(orderBy));
    }


    public Page<Passenger> getPassengerPage() {
        return new PageImpl<>(getPassengerList());
    }

    public List<PassengerResponse> getPassengerResponseList() {
        return Arrays.asList(
                getOtherPassengerResponse(),
                getDefaultPassengerResponse()
        );
    }

    public List<Passenger> getPassengerList() {
        return Arrays.asList(
                getOtherPassenger(),
                getDefaultPassenger()
        );
    }

    public CreatePassengerRequest getCreatePassengerRequest() {
        return CreatePassengerRequest.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public UpdatePassengerRequest getUpdatePassengerRequest() {
        return UpdatePassengerRequest.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .build();
    }

    public PassengerRatingMessage getPassengerRatingMessage() {
        return PassengerRatingMessage.builder()
                .passengerId(DEFAULT_ID)
                .rating(OTHER_RATING)
                .build();
    }
}
