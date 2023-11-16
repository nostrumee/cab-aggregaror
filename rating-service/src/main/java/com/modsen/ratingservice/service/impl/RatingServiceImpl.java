package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.dto.RatingResponse;
import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RatingService;
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
    private final RatingMapper ratingMapper;

    @Override
    public void ratePassenger(PassengerRatingMessage ratingMessage) {
        log.info("Rating a passenger with id {} for a ride with id {}", ratingMessage.passengerId(), ratingMessage.rideId());

        PassengerRating ratingToCreate = ratingMapper.fromRatingMessageToPassengerRating(ratingMessage);
        passengerRatingRepository.save(ratingToCreate);
    }

    @Override
    public void rateDriver(DriverRatingMessage ratingMessage) {
        log.info("Rating a driver with id {} for a ride with id {}", ratingMessage.driverId(), ratingMessage.rideId());

        DriverRating ratingToCreate = ratingMapper.fromRatingMessageToDriverRating(ratingMessage);
        driverRatingRepository.save(ratingToCreate);
    }

    @Override
    public RatingResponse getPassengerRating(long passengerId) {
        BigDecimal rating = passengerRatingRepository.findPassengerRating(passengerId);
        return RatingResponse.builder()
                .rating(rating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }

    @Override
    public RatingResponse getDriverRating(long driverId) {
        BigDecimal rating = driverRatingRepository.findDriverRating(driverId);
        return RatingResponse.builder()
                .rating(rating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();
    }
}
