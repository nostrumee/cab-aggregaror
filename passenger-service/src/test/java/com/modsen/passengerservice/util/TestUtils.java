package com.modsen.passengerservice.util;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
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
    public final String DEFAULT_LAST_NAME = "Smith";
    public final String DEFAULT_EMAIL = "johnsmith@example.com";
    public final String DEFAULT_PHONE = "123-45-67";
    public final double DEFAULT_RATING = 5.0;

    public final long OTHER_ID = 2L;
    public final String OTHER_FIRST_NAME = "Jane";
    public final String OTHER_LAST_NAME = "Doe";
    public final String OTHER_EMAIL = "janedoe@example.com";
    public final String OTHER_PHONE = "987-65-43";
    public final double OTHER_RATING = 5.0;

    public final long NEW_ID = 11L;
    public final String NEW_FIRST_NAME = "John";
    public final String NEW_LAST_NAME = "Dorian";
    public final String NEW_EMAIL = "johndorian@example.com";
    public final String NEW_PHONE = "111-22-33";
    public final double NEW_RATING = 4.65;

    public final int PAGE_NUMBER = 1;
    public final long MOCK_TOTAL = 2L;

    public final int VALID_PAGE = 1;
    public final int VALID_SIZE = 2;
    public final String VALID_ORDER_BY = "firstName";

    public final int INVALID_PAGE = -1;
    public final int INVALID_SIZE = -1;
    public final String INVALID_ORDER_BY = "invalid";

    public final String INVALID_EMAIL = "invalid@email";
    public final String INVALID_PHONE = "111111111";

    public final long NON_EXISTING_ID = 256;
    public final long TOTAL = 10L;

    public final String FIRST_NAME_VALIDATION_MESSAGE_KEY = "firstname.not.blank";
    public final String LAST_NAME_VALIDATION_MESSAGE_KEY = "lastname.not.blank";
    public final String EMAIL_VALIDATION_MESSAGE_KEY = "email.valid";
    public final String PHONE_VALIDATION_MESSAGE_KEY = "phone.valid";

    public final String GET_PASSENGER_BY_ID_PATH = "/api/v1/passengers/{id}";
    public final String GET_PASSENGER_PAGE_PATH = "/api/v1/passengers";
    public final String ADD_PASSENGER_PATH = "/api/v1/passengers";
    public final String UPDATE_PASSENGER_PATH = "/api/v1/passengers/{id}";
    public final String DELETE_PASSENGER_PATH = "/api/v1/passengers/{id}";

    public final String FIRST_NAME_FIELD_NAME = "firstName";
    public final String LAST_NAME_FIELD_NAME = "lastName";
    public final String EMAIL_FIELD_NAME = "email";
    public final String PHONE_FIELD_NAME = "phone";

    public final String PAGE_PARAM_NAME = "page";
    public final String SIZE_PARAM_NAME = "size";
    public final String ORDER_BY_PARAM_NAME = "order_by";
    public final String ID_PARAM_NAME = "id";

    public final String PASSENGER_RATING_TOPIC_NAME = "passenger-rating-topic";

    public Passenger getDefaultPassenger() {
        return Passenger.builder()
                .id(DEFAULT_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(DEFAULT_RATING)
                .build();
    }

    public Passenger getOtherPassenger() {
        return Passenger.builder()
                .id(OTHER_ID)
                .firstName(OTHER_FIRST_NAME)
                .lastName(OTHER_LAST_NAME)
                .email(OTHER_EMAIL)
                .phone(OTHER_PHONE)
                .rating(DEFAULT_RATING)
                .build();
    }

    public Passenger getNotSavedPassenger() {
        return Passenger.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .rating(DEFAULT_RATING)
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
                .rating(DEFAULT_RATING)
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
                .rating(NEW_RATING)
                .build();
    }
}
