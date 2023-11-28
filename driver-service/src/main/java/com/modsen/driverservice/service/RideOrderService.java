package com.modsen.driverservice.service;

import com.modsen.driverservice.dto.message.AcceptRideMessage;
import com.modsen.driverservice.dto.message.CreateRideMessage;

public interface RideOrderService {

    AcceptRideMessage acceptRideOrder(CreateRideMessage orderMessage);
}
