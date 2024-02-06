package com.modsen.passengerservice.security;

import com.modsen.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.modsen.passengerservice.util.SecurityUtils.*;

@Service("passengerServiceSecurityExpression")
@RequiredArgsConstructor
public class PassengerServiceSecurityExpression {

    private final PassengerRepository passengerRepository;

    public boolean canAccessPassenger(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());

        return userId.equals(id) || hasAnyRole(authentication, ADMIN_ROLE_NAME);
    }

    public boolean canSignUp() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID driverId = UUID.fromString(authentication.getName());

        return !passengerRepository.existsByExternalId(driverId) && hasAnyRole(authentication, PASSENGER_ROLE_NAME);
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
