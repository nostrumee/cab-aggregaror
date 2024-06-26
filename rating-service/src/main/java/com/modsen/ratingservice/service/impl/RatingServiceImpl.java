package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.exception.InvalidRideStatusException;
import com.modsen.ratingservice.exception.ServiceUnavailableException;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RatingService;
import com.modsen.ratingservice.service.RideService;
import com.modsen.ratingservice.service.SendMessageHandler;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.modsen.ratingservice.util.ErrorMessages.*;

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
    @Transactional
    public void ratePassenger(PassengerRatingRequest ratingRequest) {
        log.info("Rating a passenger of a ride with id {}", ratingRequest.rideId());

        RideResponse rideResponse = getAndCheckRideResponse(ratingRequest.rideId());

        PassengerRating ratingToAdd = ratingMapper.fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        passengerRatingRepository.save(ratingToAdd);

        BigDecimal updatedRating = passengerRatingRepository.findPassengerRating(rideResponse.passengerId());
        PassengerRatingMessage ratingMessage = PassengerRatingMessage.builder()
                .passengerId(rideResponse.passengerId())
                .rating(updatedRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        sendMessageHandler.handlePassengerRatingMessage(ratingMessage);
    }

    @Override
    @Transactional
    public void rateDriver(DriverRatingRequest ratingRequest) {
        log.info("Rating a driver of a ride with id {}", ratingRequest.rideId());

        RideResponse rideResponse = getAndCheckRideResponse(ratingRequest.rideId());

        DriverRating ratingToAdd = ratingMapper.fromRideResponseAndRatingRequest(rideResponse, ratingRequest);
        driverRatingRepository.save(ratingToAdd);

        BigDecimal updatedRating = driverRatingRepository.findDriverRating(rideResponse.driverId());
        DriverRatingMessage ratingMessage = DriverRatingMessage.builder()
                .driverId(rideResponse.driverId())
                .rating(updatedRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        sendMessageHandler.handleDriverRatingMessage(ratingMessage);
    }

    private RideResponse getAndCheckRideResponse(long rideId) {
        try {
            RideResponse rideResponse = rideService.getRideById(rideId);

            if (rideResponse.status() == null) {
                log.error("Ride service currently unavailable");
                throw new ServiceUnavailableException(RIDE_SERVICE_UNAVAILABLE);
            } else if (!rideResponse.status().equals(RideStatus.FINISHED)) {
                log.error("Ride order with id {} is not finished yet", rideId);
                throw new InvalidRideStatusException(RideStatus.FINISHED.name());
            }

            return rideResponse;
        } catch (CallNotPermittedException e) {
            log.error("Ride service unavailable. Reason: {}", e.getMessage());
            throw new ServiceUnavailableException(RIDE_SERVICE_UNAVAILABLE);
        }
    }
}
