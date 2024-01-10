package com.modsen.ratingservice.integration.resilience;

import com.modsen.ratingservice.client.RideClient;
import com.modsen.ratingservice.dto.response.RideResponse;
import com.modsen.ratingservice.entity.RideStatus;
import com.modsen.ratingservice.integration.IntegrationTestBase;
import com.modsen.ratingservice.service.RideService;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static com.modsen.ratingservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class CircuitBreakerIntegrationTest extends IntegrationTestBase {

    @MockBean
    private RideClient rideClient;

    private final RideService rideService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker rideServiceCircuitBreaker;

    @BeforeEach
    void setUp() {
        rideServiceCircuitBreaker = circuitBreakerRegistry.circuitBreaker(RIDE_SERVICE_NAME);
    }

    @Test
    void testRideServiceCircuitBreakerFlow() {
        doThrow(RetryableException.class)
                .when(rideClient)
                .getRideById(DEFAULT_ID);

        assertThat(rideServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        for (int i = 0; i < 10; i++) {
            RideResponse ride = rideService.getRideById(DEFAULT_ID);
            assertThat(ride.status()).isNull();
        }

        assertThat(rideServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(rideServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
                });

        doReturn(getRideResponse(RideStatus.FINISHED))
                .when(rideClient)
                .getRideById(DEFAULT_ID);

        for (int i = 0; i < 5; i++) {
            RideResponse ride = rideService.getRideById(DEFAULT_ID);
            assertThat(ride.status()).isNotNull();
        }

        assertThat(rideServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }
}
