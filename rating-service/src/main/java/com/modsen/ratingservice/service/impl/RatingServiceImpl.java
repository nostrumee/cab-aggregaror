package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.exception.InvalidRideStatusException;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RatingService;
import com.modsen.ratingservice.service.RideService;
import com.modsen.ratingservice.service.SendMessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final PassengerRatingRepository passengerRatingRepository;
    private final DriverRatingRepository driverRatingRepository;
    private final RideService rideService;
    private final SendMessageHandler sendMessageHandler;
    private final RatingMapper ratingMapper;

    @Override
    public void ratePassenger(PassengerRatingRequest ratingRequest) {
        log.info("Rating a passenger of a ride with id {}", ratingRequest.rideId());

        RideResponse rideResponse = rideService.getRideById(ratingRequest.rideId());

        if (rideResponse.status().equals(RideStatus.FINISHED)) {
            log.error("Ride order with id {} is not finished yet", ratingRequest.rideId());
            throw new InvalidRideStatusException(RideStatus.FINISHED.name());
        }

        PassengerRating ratingToAdd = ratingMapper.fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        passengerRatingRepository.save(ratingToAdd);

        BigDecimal updatedRating = passengerRatingRepository.findPassengerRating(rideResponse.passengerId());
        PassengerRatingMessage updateRatingMessage = PassengerRatingMessage.builder()
                .passengerId(rideResponse.passengerId())
                .rating(updatedRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        sendMessageHandler.handleUpdatePassengerRatingMessage(updateRatingMessage);
    }

    @Override
    public void rateDriver(DriverRatingRequest ratingRequest) {
        log.info("Rating a driver of a ride with id {}", ratingRequest.rideId());

        RideResponse rideResponse = rideService.getRideById(ratingRequest.rideId());

        DriverRating ratingToAdd = ratingMapper.fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        driverRatingRepository.save(ratingToAdd);

        BigDecimal updatedRating = driverRatingRepository.findDriverRating(rideResponse.driverId());
        DriverRatingMessage updateMessage = DriverRatingMessage.builder()
                .driverId(rideResponse.driverId())
                .rating(updatedRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        sendMessageHandler.handleUpdateDriverRatingMessage(updateMessage);
    }
}
