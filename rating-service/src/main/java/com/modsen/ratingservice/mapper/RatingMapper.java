package com.modsen.ratingservice.mapper;

import com.modsen.ratingservice.dto.message.DriverRatingMessage;
import com.modsen.ratingservice.dto.message.PassengerRatingMessage;
import com.modsen.ratingservice.entity.DriverRating;
import com.modsen.ratingservice.entity.PassengerRating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    PassengerRating fromRatingMessageToPassengerRating(PassengerRatingMessage ratingMessage);

    DriverRating fromRatingMessageToDriverRating(DriverRatingMessage ratingMessage);
}
