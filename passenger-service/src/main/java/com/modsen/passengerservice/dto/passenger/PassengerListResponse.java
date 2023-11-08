package com.modsen.passengerservice.dto.passenger;

import java.util.List;

public record PassengerListResponse (
        List<PassengerResponse> passengers
) {
}
