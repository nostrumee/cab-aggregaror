package com.modsen.e2etestsservice.dto.response;

import com.modsen.e2etestsservice.entity.DriverStatus;

public record DriverResponse(
        String firstName,
        String lastName,
        String email,
        String phone,
        Double rating,
        DriverStatus status
) {
}
