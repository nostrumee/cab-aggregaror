package com.modsen.passengerservice.dto.response;

import java.util.List;

public record PassengerListResponse (
        List<PassengerResponse> passengers
) {
}
