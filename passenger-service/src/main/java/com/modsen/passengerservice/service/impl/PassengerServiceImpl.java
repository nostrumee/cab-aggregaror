package com.modsen.passengerservice.service.impl;

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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

        checkEmailAndPhoneUnique(createRequest.email(), createRequest.phone());

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

        checkEmailAndPhoneUnique(updateRequest.email(), updateRequest.phone());

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

    private PageRequest getPageRequest(int page, int size, String orderBy) {
        if (page < 1 || size < 1) {
            log.error("Invalid request parameter passed: page: {}, size: {}", page, size);
            throw new InvalidPageParameterException();
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
        List<String> paramsList = Arrays.asList("firstName", "lastName", "email", "phone");

        if (!paramsList.contains(orderBy)) {
            log.error("Invalid sorting parameter passed: {}", orderBy);

            String acceptableParams = String.join(", ", paramsList);
            throw new InvalidSortingParameterException(orderBy, acceptableParams);
        }
    }

    private void checkEmailAndPhoneUnique(String email, String phone) {
        if (passengerRepository.existsByEmail(email)) {
            log.error("Email {} is already taken", email);
            throw new EmailTakenException(email);
        }

        if (passengerRepository.existsByPhone(phone)) {
            log.error("Phone {} is already taken", phone);
            throw new PhoneTakenException(phone);
        }
    }
}
