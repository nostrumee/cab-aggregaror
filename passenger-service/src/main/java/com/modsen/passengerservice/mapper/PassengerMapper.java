package com.modsen.passengerservice.mapper;

import com.modsen.passengerservice.dto.passenger.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.passenger.response.PassengerResponse;
import com.modsen.passengerservice.dto.passenger.request.UpdatePassengerRequest;
import com.modsen.passengerservice.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerResponse fromEntityToResponse(Passenger entity);

    Passenger fromCreateRequestToEntity(CreatePassengerRequest createRequest);

    void updateEntityFromUpdateRequest(UpdatePassengerRequest updateRequest, @MappingTarget Passenger entity);

    List<PassengerResponse> fromEntityListToResponseList(List<Passenger> passengers);
}
