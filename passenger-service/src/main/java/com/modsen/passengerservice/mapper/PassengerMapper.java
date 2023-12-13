package com.modsen.passengerservice.mapper;

import com.modsen.passengerservice.dto.request.CreatePassengerRequest;
import com.modsen.passengerservice.dto.response.PassengerResponse;
import com.modsen.passengerservice.dto.request.UpdatePassengerRequest;
import com.modsen.passengerservice.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PassengerMapper {

    PassengerResponse fromEntityToResponse(Passenger entity);

    @Mapping(target = "rating", constant = "5.0")
    Passenger fromCreateRequestToEntity(CreatePassengerRequest createRequest);

    void updateEntityFromUpdateRequest(UpdatePassengerRequest updateRequest, @MappingTarget Passenger entity);

    List<PassengerResponse> fromEntityListToResponseList(List<Passenger> passengers);
}
