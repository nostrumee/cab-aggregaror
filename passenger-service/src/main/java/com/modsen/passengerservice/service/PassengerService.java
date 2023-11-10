package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import org.springframework.data.domain.PageRequest;

public interface PassengerService {

    PassengerPageResponse getAllPassengers(PageRequest pageRequest);

    PassengerResponse getById(Long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, Long id);

    void deletePassenger(Long id);
}
