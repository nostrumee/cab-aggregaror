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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.stream.IntStream;

import static com.modsen.ratingservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
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

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(rideServiceCircuitBreaker.getState());

        IntStream.range(0, 10).forEach((i) -> {
            RideResponse ride = rideService.getRideById(DEFAULT_ID);
            assertThat(ride.driverId()).isEqualTo(0L);
        });

        assertThat(CircuitBreaker.State.OPEN).isEqualTo(rideServiceCircuitBreaker.getState());

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(CircuitBreaker.State.HALF_OPEN).isEqualTo(rideServiceCircuitBreaker.getState());
                });

        doReturn(getRideResponse(RideStatus.FINISHED))
                .when(rideClient)
                .getRideById(DEFAULT_ID);

        IntStream.range(0, 5).forEach((i) -> {
            RideResponse ride = rideService.getRideById(DEFAULT_ID);
            assertThat(ride.driverId()).isNotEqualTo(0L);
        });

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(rideServiceCircuitBreaker.getState());
    }
}
