package com.modsen.ratingservice.security;

import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("ratingServiceSecurityExpression")
@RequiredArgsConstructor
public class RatingServiceSecurityExpression {

    private final RideService rideService;

    public boolean canRatePassenger(long id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());
        RideResponse rideResponse = rideService.getRideById(id);

        return userId.equals(rideResponse.driverId());
    }

    public boolean canRateDriver(long id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());
        RideResponse rideResponse = rideService.getRideById(id);

        return userId.equals(rideResponse.passengerId());
    }
}
