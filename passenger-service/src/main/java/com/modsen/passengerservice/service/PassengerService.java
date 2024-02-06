package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;

import java.util.UUID;

public interface PassengerService {

    PassengerPageResponse getPassengerPage(int page, int size, String orderBy);

    PassengerResponse getById(UUID id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest, UUID externalId);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, UUID id);

    void deletePassenger(UUID id);

    void updatePassengerRating(PassengerRatingMessage updateRatingMessage);
}
