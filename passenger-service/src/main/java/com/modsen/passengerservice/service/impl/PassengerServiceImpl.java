package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.passenger.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.passenger.response.PassengerListResponse;
import com.modsen.passengerservice.dto.passenger.response.PassengerResponse;
import com.modsen.passengerservice.dto.passenger.request.UpdatePassengerRequest;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerListResponse getAllPassengers() {
        log.info("Retrieving all passengers");

        List<Passenger> retrievedPassengers = passengerRepository.findAll();
        List<PassengerResponse> passengers =
                passengerMapper.fromEntityListToResponseList(retrievedPassengers);

        return new PassengerListResponse(passengers);
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
}
