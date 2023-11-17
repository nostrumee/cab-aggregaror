package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerPageResponse;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.dto.response.RatingResponse;
import org.springframework.data.domain.PageRequest;

public interface PassengerService {

    PassengerPageResponse getPassengerPage(int page, int size, String orderBy);

    PassengerResponse getById(long id);

    PassengerResponse addPassenger(CreatePassengerRequest createRequest);

    PassengerResponse updatePassenger(UpdatePassengerRequest updateRequest, long id);

    void deletePassenger(long id);

    RatingResponse getPassengerRating(long id);
}
