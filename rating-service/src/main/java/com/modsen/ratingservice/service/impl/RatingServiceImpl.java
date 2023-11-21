package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.message.DriverRatingMessage;
import com.modsen.ratingservice.message.PassengerRatingMessage;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.message.UpdateDriverRatingMessage;
import com.modsen.ratingservice.message.UpdatePassengerRatingMessage;
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

        PassengerRating ratingToAdd = ratingMapper.fromRatingMessageToPassengerRating(ratingMessage);
        passengerRatingRepository.save(ratingToAdd);

        BigDecimal updatedRating = passengerRatingRepository.findPassengerRating(ratingMessage.passengerId());
        UpdatePassengerRatingMessage updateMessage = UpdatePassengerRatingMessage.builder()
                .passengerId(ratingMessage.passengerId())
                .rating(updatedRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        // TODO: send it to update-passenger-rating topic
    }

    @Override
    public void rateDriver(DriverRatingMessage ratingMessage) {
        log.info("Rating a driver with id {} for a ride with id {}", ratingMessage.driverId(), ratingMessage.rideId());

        DriverRating ratingToAdd = ratingMapper.fromRatingMessageToDriverRating(ratingMessage);
        driverRatingRepository.save(ratingToAdd);

        BigDecimal updatedRating = driverRatingRepository.findDriverRating(ratingMessage.driverId());
        UpdateDriverRatingMessage updateMessage = UpdateDriverRatingMessage.builder()
                .driverId(ratingMessage.driverId())
                .rating(updatedRating.setScale(2, RoundingMode.HALF_UP).doubleValue())
                .build();

        // TODO: send it to update-driver-rating topic
    }
}
