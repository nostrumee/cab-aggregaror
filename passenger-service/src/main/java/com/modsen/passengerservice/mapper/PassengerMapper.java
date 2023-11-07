package com.modsen.passengerservice.mapper;

import com.modsen.passengerservice.dto.PassengerDTO;
import com.modsen.passengerservice.dto.PassengerListDTO;
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

    PassengerDTO toDTO(Passenger passenger);

    Passenger toEntity(PassengerDTO passengerDTO);

    void updatePassengerFromDTO(PassengerDTO passengerDTO, @MappingTarget Passenger passenger);

    PassengerListDTO toPassengerList(List<Passenger> passengers);
}
