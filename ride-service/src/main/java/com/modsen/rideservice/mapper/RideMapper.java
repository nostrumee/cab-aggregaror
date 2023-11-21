package com.modsen.rideservice.mapper;

import com.modsen.rideservice.dto.request.CreateRideRequest;
import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.entity.Ride;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RideMapper {

    RideResponse fromEntityToResponse(Ride entity);

    Ride fromCreateRequestToEntity(CreateRideRequest createRequest);

    List<RideResponse> fromEntityListToResponseList(List<Ride> rides);
}
