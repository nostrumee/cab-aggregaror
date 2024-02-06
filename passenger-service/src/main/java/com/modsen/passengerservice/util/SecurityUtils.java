package com.modsen.passengerservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {
    public static final String[] OPENAPI_REQUEST_MATCHERS = {
            "/swagger-ui/**",
            "/passenger-service/v3/api-docs/**"
    };

    public static final String ACTUATOR_REQUEST_MATCHER = "/actuator/**";

    public static final String AUTHORITY_PREFIX = "ROLE_";

    public static final String AUTHORITIES_CLAIM_NAME = "roles";

    public static final String ADMIN_ROLE_NAME = "ADMIN";

    public static final String PASSENGER_ROLE_NAME = "PASSENGER";
}
