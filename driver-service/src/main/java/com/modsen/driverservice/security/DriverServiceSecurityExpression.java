package com.modsen.driverservice.security;

import com.modsen.driverservice.exception.DriverNotFoundException;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.modsen.driverservice.util.SecurityUtils.*;

@Service("driverServiceSecurityExpression")
@RequiredArgsConstructor
public class DriverServiceSecurityExpression {

    private final DriverRepository driverRepository;

    public boolean canAccessDriver(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID userId = UUID.fromString(authentication.getName());

        return userId.equals(id) || hasAnyRole(authentication, ADMIN_ROLE_NAME);
    }

    public boolean canSignUp() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        UUID driverId = UUID.fromString(authentication.getName());

        return !driverRepository.existsByExternalId(driverId) && hasAnyRole(authentication, DRIVER_ROLE_NAME);
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
