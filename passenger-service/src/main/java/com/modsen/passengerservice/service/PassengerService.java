package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerListResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import org.springframework.data.domain.PageRequest;

public interface PassengerService {

    PassengerListResponse getAllPassengers(PageRequest pageRequest);

    PassengerResponse getById(Long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, Long id);

    void deletePassenger(Long id);
}
