package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.*;

public interface PassengerService {

    PassengerListResponse getAllPassengers();

    PassengerResponse getById(Long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, Long id);

    void deletePassenger(Long id);
}
