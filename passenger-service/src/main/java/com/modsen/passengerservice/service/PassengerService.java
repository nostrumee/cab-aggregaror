package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerListResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;

public interface PassengerService {

    PassengerListResponse getAllPassengers();

    PassengerResponse getById(Long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, Long id);

    void deletePassenger(Long id);
}
