package com.modsen.rideservice.mapper;

import com.modsen.rideservice.dto.request.CreateRideOrderRequest;
import com.modsen.rideservice.dto.response.RideOrderResponse;
import com.modsen.rideservice.entity.RideOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RideOrderMapper {

    RideOrderResponse fromEntityToResponse(RideOrder entity);

    RideOrder fromCreateRequestToEntity(CreateRideOrderRequest createRequest);
}
