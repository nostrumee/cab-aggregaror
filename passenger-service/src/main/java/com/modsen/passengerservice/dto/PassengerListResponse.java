package com.modsen.passengerservice.dto;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PassengerListResponse {
    private List<PassengerResponse> passengers;
}
