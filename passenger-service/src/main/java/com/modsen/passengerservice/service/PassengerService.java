package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.message.PassengerRatingMessage;
import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;

public interface PassengerService {

    PassengerPageResponse getPassengerPage(int page, int size, String orderBy);

    PassengerResponse getById(long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, long id);

    void deletePassenger(long id);

    void updatePassengerRating(PassengerRatingMessage updateRatingMessage);
}
