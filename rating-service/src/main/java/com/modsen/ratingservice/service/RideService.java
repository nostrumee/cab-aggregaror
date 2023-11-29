package com.modsen.ratingservice.service;

import com.modsen.ratingservice.dto.response.RideResponse;

public interface RideService {

    RideResponse getRideById(long id);
}
