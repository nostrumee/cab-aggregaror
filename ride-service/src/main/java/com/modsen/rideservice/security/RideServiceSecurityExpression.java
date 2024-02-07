package com.modsen.rideservice.security;

import com.modsen.rideservice.dto.response.RideResponse;
import com.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.modsen.rideservice.util.SecurityUtils.*;

@Service("rideServiceSecurityExpression")
@RequiredArgsConstructor
public class RideServiceSecurityExpression {

    private final RideService rideService;

    public boolean canAccessRideHistory(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());

        return userId.equals(id) || hasAnyRole(authentication, ADMIN_ROLE_NAME);
    }

    public boolean canAccessRide(long id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());

        RideResponse ride = rideService.getById(id);

        return userId.equals(ride.driverId())
                || userId.equals(ride.passengerId())
                || hasAnyRole(authentication, ADMIN_ROLE_NAME);
    }

    public boolean canChangeRideStatus(long id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());

        RideResponse ride = rideService.getById(id);

        return userId.equals(ride.driverId());
    }

    public boolean canCreateRide(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());

        return id.equals(userId) && hasAnyRole(authentication, PASSENGER_ROLE_NAME);
    }

    private boolean hasAnyRole(Authentication authentication, String... roles) {
        for (String role : roles) {
            GrantedAuthority authority
                    = new SimpleGrantedAuthority(AUTHORITY_PREFIX + role);

            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }

        return false;
    }
}
