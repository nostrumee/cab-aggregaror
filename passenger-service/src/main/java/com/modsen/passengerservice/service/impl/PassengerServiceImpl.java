package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.PassengerDTO;
import com.modsen.passengerservice.dto.PassengerListDTO;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.exception.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerListDTO getAllPassengers() {
        log.info("Retrieving all passengers");
        List<PassengerDTO> passengers = passengerMapper.toListDTO(passengerRepository.findAll());
        return new PassengerListDTO(passengers);
    }

    @Override
    public PassengerDTO getById(Long id) {
        log.info("Retrieving passenger by id {}", id);

        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            return passengerMapper.toDTO(optionalPassenger.get());
        } else {
            log.error("Passenger with id {} was not found", id);
            throw new PassengerNotFoundException(id);
        }
    }

    @Override
    public Long addPassenger(PassengerDTO passengerDTO) {
        log.info("Adding passenger");

        Passenger passenger = passengerRepository.save(passengerMapper.toEntity(passengerDTO));
        return passenger.getId();
    }

    @Override
    public PassengerDTO updatePassenger(PassengerDTO passengerDTO, Long id) {
        log.info("Updating passenger with id {}", id);

        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            passengerMapper.updatePassengerFromDTO(passengerDTO, passenger);
            passengerRepository.save(passenger);

            return passengerMapper.toDTO(passenger);
        } else {
            log.error("Passenger with id {} was not found", id);
            throw new PassengerNotFoundException(id);
        }
    }

    @Override
    public void deletePassenger(Long id) {
        log.info("Deleting passenger with id {}", id);

        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            passengerRepository.delete(optionalPassenger.get());
        } else {
            log.error("Passenger with id {} was not found", id);
            throw new PassengerNotFoundException(id);
        }
    }
}
