package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.*;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

import static com.modsen.passengerservice.util.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerPageResponse getPassengerPage(int page, int size, String orderBy) {
        log.info("Retrieving passengers page");

        PageRequest pageRequest = getPageRequest(page, size, orderBy);
        Page<Passenger> passengersPage = passengerRepository.findAll(pageRequest);

        List<Passenger> retrievedPassengers = passengersPage.getContent();
        Long total = passengersPage.getTotalElements();

        List<PassengerResponse> passengers =
                passengerMapper.fromEntityListToResponseList(retrievedPassengers);

        return PassengerPageResponse.builder()
                .passengers(passengers)
                .pageNumber(page)
                .total(total)
                .build();
    }

    @Override
    public PassengerResponse getById(long id) {
        log.info("Retrieving passenger by id {}", id);

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Passenger with id {} was not found", id);
                    return new PassengerNotFoundException(id);
                });

        return passengerMapper.fromEntityToResponse(passenger);
    }

    @Override
    public PassengerResponse addPassenger(CreatePassengerRequest createRequest) {
        log.info("Adding passenger");

        checkCreateDataUnique(createRequest);

        Passenger passengerToCreate = passengerMapper.fromCreateRequestToEntity(createRequest);
        Passenger createdPassenger = passengerRepository.save(passengerToCreate);

        return passengerMapper.fromEntityToResponse(createdPassenger);
    }

    @Override
    public PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, long id) {
        log.info("Updating passenger with id {}", id);

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Passenger with id {} was not found", id);
                    return new PassengerNotFoundException(id);
                });

        checkUpdateDataUnique(updateRequest, passenger);

        passengerMapper.updateEntityFromUpdateRequest(updateRequest, passenger);
        passengerRepository.save(passenger);

        return passengerMapper.fromEntityToResponse(passenger);
    }

    @Override
    public void deletePassenger(long id) {
        log.info("Deleting passenger with id {}", id);

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Passenger with id {} was not found", id);
                    return new PassengerNotFoundException(id);
                });

        passengerRepository.delete(passenger);
    }

    @Override
    public void updatePassengerRating(PassengerRatingMessage updateRatingMessage) {
        long id = updateRatingMessage.passengerId();

        log.info("Updating rating of passenger with id {}", id);

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Passenger with id {} was not found", id);
                    return new PassengerNotFoundException(id);
                });

        passenger.setRating(updateRatingMessage.rating());
        passengerRepository.save(passenger);
    }

    private PageRequest getPageRequest(int page, int size, String orderBy) {
        if (page < 1 || size < 1) {
            log.error("Invalid request parameter passed: page: {}, size: {}", page, size);
            throw new InvalidRequestParamException(INVALID_PAGE_PARAMETERS_MESSAGE);
        }

        PageRequest pageRequest;
        if (orderBy == null) {
            pageRequest = PageRequest.of(page - 1, size);
        } else {
            validateSortingParameter(orderBy);
            pageRequest = PageRequest.of(page - 1, size, Sort.by(orderBy));
        }

        return pageRequest;
    }

    private void validateSortingParameter(String orderBy) {
        List<String> fieldNames = Arrays.stream(PassengerResponse.class.getDeclaredFields())
                .map(Field::getName)
                .toList();

        if (!fieldNames.contains(orderBy)) {
            String acceptableParams = String.join(", ", fieldNames);
            String errorMessage = String.format(INVALID_SORTING_PARAMETER_MESSAGE, orderBy, acceptableParams);
            throw new InvalidRequestParamException(errorMessage);
        }
    }

    private void checkEmailUnique(String email, Map<String, String> errors) {
        if (passengerRepository.existsByEmail(email)) {
            log.error("Passenger with email {} already exists", email);
            errors.put(
                    "email",
                    String.format(PASSENGER_WITH_EMAIL_EXISTS_MESSAGE, email)
            );
        }
    }

    private void checkPhoneUnique(String phone, Map<String, String> errors) {
        if (passengerRepository.existsByPhone(phone)) {
            log.error("Passenger with phone {} already exists", phone);
            errors.put(
                    "phone",
                    String.format(PASSENGER_WITH_PHONE_EXISTS_MESSAGE, phone)
            );
        }
    }

    private void checkCreateDataUnique(CreatePassengerRequest createRequest) {
        var errors = new HashMap<String, String>();

        checkEmailUnique(createRequest.email(), errors);
        checkPhoneUnique(createRequest.phone(), errors);

        if (!errors.isEmpty()) {
            throw new PassengerAlreadyExistsException(errors);
        }
    }

    private void checkUpdateDataUnique(UpdatePassengerRequest updateRequest, Passenger passenger) {
        var errors = new HashMap<String, String>();

        if (!Objects.equals(updateRequest.email(), passenger.getEmail())) {
            checkEmailUnique(updateRequest.email(), errors);
        }

        if (!Objects.equals(updateRequest.phone(), passenger.getPhone())) {
            checkPhoneUnique(updateRequest.phone(), errors);
        }

        if (!errors.isEmpty()) {
            throw new PassengerAlreadyExistsException(errors);
        }
    }
}
