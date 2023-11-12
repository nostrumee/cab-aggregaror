package com.modsen.driverservice.dto.request;

import lombok.Builder;

@Builder
public record UpdateDriverRequest(
        String firstName,
        String lastName,
        String licenceNumber,
        String email,
        String phone
) {
}
