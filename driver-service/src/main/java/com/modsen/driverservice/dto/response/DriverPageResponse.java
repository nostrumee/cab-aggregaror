package com.modsen.driverservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record DriverPageResponse(
        List<DriverResponse> drivers,
        Integer pageNumber,
        Integer total
) {
}
