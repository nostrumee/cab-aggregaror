package com.modsen.rideservice.mapper;

import com.modsen.rideservice.dto.message.RideStatusMessage;
import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.entity.Ride;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mappings({
            @Mapping(source = "passenger.firstName", target = "passengerFirstName"),
            @Mapping(source = "passenger.email", target = "passengerEmail"),
            @Mapping(source = "ride.id", target = "rideId")
    })
    RideStatusMessage fromRideAndPassengerResponse(Ride ride, PassengerResponse passenger);
}
