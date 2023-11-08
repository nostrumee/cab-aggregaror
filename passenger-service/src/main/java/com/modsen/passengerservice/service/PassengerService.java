package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.passenger.CreatePassengerRequest;
import com.modsen.passengerservice.dto.passenger.PassengerListResponse;
import com.modsen.passengerservice.dto.passenger.PassengerResponse;
import com.modsen.passengerservice.dto.passenger.UpdatePassengerRequest;

public interface PassengerService {

    PassengerListResponse getAllPassengers();

    PassengerResponse getById(Long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, Long id);

    void deletePassenger(Long id);
}
