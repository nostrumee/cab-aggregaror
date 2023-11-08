package com.modsen.passengerservice.mapper;

import com.modsen.passengerservice.dto.CreatePassengerRequest;
import com.modsen.passengerservice.dto.PassengerResponse;
import com.modsen.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.passengerservice.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PassengerMapper {

    PassengerResponse fromEntityToResponse(Passenger entity);

    Passenger fromCreateRequestToEntity(CreatePassengerRequest createRequest);

    void updateEntityFromUpdateRequest(UpdatePassengerRequest updateRequest, @MappingTarget Passenger entity);

    List<PassengerResponse> fromEntityListToResponseList(List<Passenger> passengers);
}
