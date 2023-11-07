package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.PassengerDTO;
import com.modsen.passengerservice.dto.PassengerListDTO;

public interface PassengerService {

    PassengerListDTO getAllPassengers();

    PassengerDTO getById(Long id);

    Long addPassenger(PassengerDTO passengerDTO);

    PassengerDTO updatePassenger(PassengerDTO passengerDTO, Long id);

    void deletePassenger(Long id);
}
