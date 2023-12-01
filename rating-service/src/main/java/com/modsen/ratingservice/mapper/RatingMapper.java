package com.modsen.ratingservice.mapper;

import com.modsen.ratingservice.dto.request.DriverRatingRequest;
import com.modsen.ratingservice.dto.request.PassengerRatingRequest;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    PassengerRating fromRideResponseAndRatingRequest(
            RideResponse rideResponse,
            PassengerRatingRequest ratingRequest
    );

    DriverRating fromRideResponseAndRatingRequest(
            RideResponse rideResponse,
            DriverRatingRequest ratingRequest
    );
}
