package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerListResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.EmailTakenException;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.exception.PhoneTakenException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerListResponse getAllPassengers(PageRequest pageRequest) {
        log.info("Retrieving all passengers");

        Page<Passenger> passengersPage = passengerRepository.findAll(pageRequest);
        List<Passenger> retrievedPassengers = passengersPage.getContent();
        Long total = passengersPage.getTotalElements();

        List<PassengerResponse> passengers =
                passengerMapper.fromEntityListToResponseList(retrievedPassengers);

        return PassengerListResponse.builder()
                .passengers(passengers)
                .total(total)
                .build();
    }

    @Override
    public PassengerResponse getById(Long id) {
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

        String email = createRequest.email();
        String phone = createRequest.phone();
        validateEmailAndPhone(email, phone);

        Passenger passengerToCreate = passengerMapper.fromCreateRequestToEntity(createRequest);
        Passenger createdPassenger = passengerRepository.save(passengerToCreate);

        return passengerMapper.fromEntityToResponse(createdPassenger);
    }

    @Override
    public PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, Long id) {
        log.info("Updating passenger with id {}", id);

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Passenger with id {} was not found", id);
                    return new PassengerNotFoundException(id);
                });

        String email = updateRequest.email();
        String phone = updateRequest.phone();
        validateEmailAndPhone(email, phone);

        passengerMapper.updateEntityFromUpdateRequest(updateRequest, passenger);
        passengerRepository.save(passenger);

        return passengerMapper.fromEntityToResponse(passenger);
    }

    @Override
    public void deletePassenger(Long id) {
        log.info("Deleting passenger with id {}", id);

        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Passenger with id {} was not found", id);
                    return new PassengerNotFoundException(id);
                });

        passengerRepository.delete(passenger);
    }

    private void validateEmailAndPhone(String email, String phone) {
        passengerRepository.findByEmail(email)
                .ifPresent(passenger -> {
                    log.error("Email {} is already taken", email);
                    throw new EmailTakenException(email);
                });

        passengerRepository.findByPhone(phone)
                .ifPresent(passenger -> {
                    log.error("Phone {} is already taken", phone);
                    throw new PhoneTakenException(phone);
                });
    }
}
