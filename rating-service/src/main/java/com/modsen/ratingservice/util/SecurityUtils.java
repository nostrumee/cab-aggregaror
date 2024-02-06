package com.modsen.ratingservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {
    public static final String[] OPENAPI_REQUEST_MATCHERS = {
            "/swagger-ui/**",
            "/driver-service/v3/api-docs/**"
    };

    public static final String ACTUATOR_REQUEST_MATCHER = "/actuator/**";

    public static final String AUTHORITY_PREFIX = "ROLE_";

    public static final String AUTHORITIES_CLAIM_NAME = "roles";

    public static final String DRIVER_ROLE_NAME = "DRIVER";

    public static final String PASSENGER_ROLE_NAME = "PASSENGER";
}