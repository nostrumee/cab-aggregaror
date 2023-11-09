package com.modsen.passengerservice.dto.passenger.response;

import java.util.List;

public record PassengerListResponse (
        List<PassengerResponse> passengers
) {
}
