package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.message.RideOrderMessage;

public interface RideOrderService {

    void acceptRideOrder(RideOrderMessage orderMessage);
}
