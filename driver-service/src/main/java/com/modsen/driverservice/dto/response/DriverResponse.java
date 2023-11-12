package com.modsen.driverservice.dto.response;

import lombok.Builder;

@Builder
public record DriverResponse(
        Long id,
        String firstName,
        String lastName,
        String licenceNumber,
        String email,
        String phone
) {
}
