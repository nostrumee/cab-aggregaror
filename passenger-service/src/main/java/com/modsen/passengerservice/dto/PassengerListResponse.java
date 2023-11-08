package com.modsen.passengerservice.dto;

import java.util.List;

public record PassengerListResponse (
        List<PassengerResponse> passengers
) {
}
