package com.modsen.edgeservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {
    public static final String[] OPENAPI_PATH_MATCHERS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/driver-service/v3/api-docs/**",
            "/passenger-service/v3/api-docs/**",
            "/ride-service/v3/api-docs/**",
            "/rating-service/v3/api-docs/**",
    };

    public static final String ACTUATOR_PATH_MATCHER = "/actuator/**";

    public static final String POST_LOGOUT_REDIRECT_URI = "{baseUrl}";
}
