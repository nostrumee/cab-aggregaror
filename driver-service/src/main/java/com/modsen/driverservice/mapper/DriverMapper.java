package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.request.CreateDriverRequest;
import com.modsen.driverservice.dto.request.UpdateDriverRequest;
import com.modsen.driverservice.dto.response.DriverResponse;
import com.modsen.driverservice.entity.Driver;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DriverMapper {

    DriverResponse fromEntityToResponse(Driver entity);

    @Mappings(value = {
            @Mapping(target = "rating", constant = "5.0"),
            @Mapping(target = "status", constant = "AVAILABLE")
    })
    Driver fromCreateRequestToEntity(CreateDriverRequest createRequest);

    void updateEntityFromUpdateRequest(UpdateDriverRequest updateRequest, @MappingTarget Driver entity);

    List<DriverResponse> fromEntityListToResponseList(List<Driver> drivers);
}
