package com.modsen.rideservice.integration.resilience;

import com.modsen.rideservice.client.DriverClient;
import com.modsen.rideservice.client.PassengerClient;
import com.modsen.rideservice.dto.response.DriverResponse;
import com.modsen.rideservice.dto.response.PassengerResponse;
import com.modsen.rideservice.integration.IntegrationTestBase;
import com.modsen.rideservice.service.DriverService;
import com.modsen.rideservice.service.PassengerService;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;

import static com.modsen.rideservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
public class CircuitBreakerIntegrationTest extends IntegrationTestBase {

    @MockBean
    private DriverClient driverClient;

    @MockBean
    private PassengerClient passengerClient;

    private final DriverService driverService;
    private final PassengerService passengerService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    private CircuitBreaker driverServiceCircuitBreaker;
    private CircuitBreaker passengerServiceCircuitBreaker;

    @BeforeEach
    void setUp() {
        driverServiceCircuitBreaker = circuitBreakerRegistry.circuitBreaker(DRIVER_SERVICE_NAME);
        passengerServiceCircuitBreaker = circuitBreakerRegistry.circuitBreaker(PASSENGER_SERVICE_NAME);
    }

    @Test
    void testDriverServiceCircuitBreakerFlow() {
        doThrow(RetryableException.class)
                .when(driverClient)
                .getDriverById(DEFAULT_DRIVER_ID);

        assertThat(driverServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        for (int i = 0; i < 10; i++) {
            DriverResponse driver = driverService.getDriverById(DEFAULT_DRIVER_ID);
            assertThat(driver.firstName()).isEqualTo("fallback");
        }

        assertThat(driverServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(driverServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
                });

        doReturn(getDriverResponse())
                .when(driverClient)
                .getDriverById(DEFAULT_DRIVER_ID);

        for (int i = 0; i < 5; i++) {
            DriverResponse driver = driverService.getDriverById(DEFAULT_DRIVER_ID);
            assertThat(driver.firstName()).isNotEqualTo("fallback");
        }

        assertThat(driverServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    void testPassengerServiceCircuitBreakerFlow() {
        doThrow(RetryableException.class)
                .when(passengerClient)
                .getPassengerById(DEFAULT_PASSENGER_ID);

        assertThat(passengerServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);

        for (int i = 0; i < 10; i++) {
            PassengerResponse passenger = passengerService.getPassengerById(DEFAULT_PASSENGER_ID);
            assertThat(passenger.firstName()).isEqualTo("fallback");
        }

        assertThat(passengerServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(passengerServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
                });

        doReturn(getPassengerResponse())
                .when(passengerClient)
                .getPassengerById(DEFAULT_PASSENGER_ID);

        for (int i = 0; i < 5; i++) {
            PassengerResponse passenger = passengerService.getPassengerById(DEFAULT_PASSENGER_ID);
            assertThat(passenger.firstName()).isNotEqualTo("fallback");
        }

        assertThat(passengerServiceCircuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }
}
