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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.util.stream.IntStream;

import static com.modsen.rideservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
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
                .getDriverById(DEFAULT_ID);

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(driverServiceCircuitBreaker.getState());

        IntStream.range(0, 10).forEach((i) -> {
            DriverResponse driver = driverService.getDriverById(DEFAULT_ID);
            assertThat(driver.firstName()).isEqualTo("fallback");
        });

        assertThat(CircuitBreaker.State.OPEN).isEqualTo(driverServiceCircuitBreaker.getState());

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(CircuitBreaker.State.HALF_OPEN).isEqualTo(driverServiceCircuitBreaker.getState());
                });

        doReturn(getDriverResponse())
                .when(driverClient)
                .getDriverById(DEFAULT_ID);

        IntStream.range(0, 5).forEach((i) -> {
            DriverResponse driver = driverService.getDriverById(DEFAULT_ID);
            assertThat(driver.firstName()).isNotEqualTo("fallback");
        });

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(driverServiceCircuitBreaker.getState());
    }

    @Test
    void testPassengerServiceCircuitBreakerFlow() {
        doThrow(RetryableException.class)
                .when(passengerClient)
                .getPassengerById(DEFAULT_ID);

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(passengerServiceCircuitBreaker.getState());

        IntStream.range(0, 10).forEach((i) -> {
            PassengerResponse passenger = passengerService.getPassengerById(DEFAULT_ID);
            assertThat(passenger.firstName()).isEqualTo("fallback");
        });

        assertThat(CircuitBreaker.State.OPEN).isEqualTo(passengerServiceCircuitBreaker.getState());

        await()
                .pollInterval(Duration.ofSeconds(5))
                .atMost(20, SECONDS)
                .untilAsserted(() -> {
                    assertThat(CircuitBreaker.State.HALF_OPEN).isEqualTo(passengerServiceCircuitBreaker.getState());
                });

        doReturn(getPassengerResponse())
                .when(passengerClient)
                .getPassengerById(DEFAULT_ID);

        IntStream.range(0, 5).forEach((i) -> {
            PassengerResponse passenger = passengerService.getPassengerById(DEFAULT_ID);
            assertThat(passenger.firstName()).isNotEqualTo("fallback");
        });

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(passengerServiceCircuitBreaker.getState());
    }
}
