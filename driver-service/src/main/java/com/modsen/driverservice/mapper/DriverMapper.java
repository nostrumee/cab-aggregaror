package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DriverMapper {

    DriverResponse fromEntityToResponse(Driver entity);

    Driver fromCreateRequestToEntity(CreateDriverRequest createRequest);

    void updateEntityFromUpdateRequest(UpdateDriverRequest updateRequest, @MappingTarget Driver entity);

    List<DriverResponse> fromEntityListToResponseList(List<Driver> drivers);
}
