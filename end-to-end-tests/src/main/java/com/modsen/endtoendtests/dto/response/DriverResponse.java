package com.modsen.endtoendtests.dto.response;

import com.modsen.endtoendtests.entity.DriverStatus;

public record DriverResponse(
        String firstName,
        String lastName,
        String email,
        String phone,
        Double rating,
        DriverStatus status
) {
}
